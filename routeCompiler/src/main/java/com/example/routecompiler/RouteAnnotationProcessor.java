package com.example.routecompiler;

import com.example.routeannotation.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 编译时注解生成类
 * Created by mj on 2021/2/9 16:11
 */
@AutoService(Processor.class)
public class RouteAnnotationProcessor extends AbstractProcessor {

    private static final String MODULE_NAME_KEY = "module_name";
    private static final String MODULE_MAIN_KEY = "module_main";
    private static final String MODULES_LIST = "modules_list";
    private static final String MODULE_MAIN_VALUE = "yes";

    // 用来生成Java文件
    private Filer filer;
    // 用来在编译期间打印信息
    private Messager messager;
    // 每个module的名称
    private String moduleName;
    // 是不是主项目module
    private String moduleMain;
    // 所有module使用&符号拼接起来的字符串，如："app&module&module2"
    private String modules;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        // 获取gradle中配置的参数
        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null && !options.isEmpty()) {
            moduleName = options.get(MODULE_NAME_KEY);
            moduleMain = options.get(MODULE_MAIN_KEY);
            modules = options.get(MODULES_LIST);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 如果一个module下有Route注解则会走到process方法，否则不会
        generateInit();
        generateList(roundEnvironment);
        return true;
    }

    /**
     * 生成RouteInit文件
     * 在该文件中生成一个静态方法，有多少个module中使用了Route，则为每个module调用RouterSaving_moduleName.add()。
     * 这样在RouterSaving_moduleName中向集合中存入Activity信息的代码就会生成。
     * 需要注意的是：这里只是编译阶段执行的，真正存入集合是在运行阶段，所以会在Routers类的init方法中动态执行
     */
    private void generateInit() {
        // 如果是主module才去生成RouterInit类
        if (MODULE_MAIN_VALUE.equals(moduleMain)) {
            // 声明RouterInit中的init方法
            MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
            // 在init方法中要调用所有module下RouterSaving_的add方法
            if (modules != null && !"".equals(modules)) {
                String[] moduleList = modules.split("&");
                if (moduleList.length > 0) {
                    for (String module : moduleList) {
                        initMethod.addStatement("RouterSaving_" + module + ".add()");
                    }
                }
            }

            // 声明RouterInit类
            TypeSpec routerInit = TypeSpec.classBuilder("RouterInit")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(initMethod.build())
                    .build();

            try {
                JavaFile.builder("com.example.route", routerInit)
                        .build()
                        .writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成RouteSaving_moduleName文件
     * 在该文件中生成一个静态方法，遍历当前module中有多少个Activity被Route注解修饰了，然后拿到当前Activity
     * 的path和class对象，存入到集合中。该静态方法的调用是在上面的RouteInit类中，这里仅是向静态方法中写入代码
     */
    private void generateList(RoundEnvironment roundEnvironment) {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
        for (Element element : elements) {
            Route annotation = element.getAnnotation(Route.class);
            String path = annotation.value();
            ClassName className = ClassName.get((TypeElement) element);
            initMethod.addStatement("com.example.routeapi.Routers.addRoute($S, $T.class)", path, className);
        }

        TypeSpec routerInit = TypeSpec.classBuilder("RouterSaving_" + moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();

        try {
            JavaFile.builder("com.example.route", routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        // 指定当前module下只支持Route注解
        set.add(Route.class.getCanonicalName());
        return set;
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> set = new HashSet<>();
        set.add(MODULE_NAME_KEY);
        set.add(MODULE_MAIN_KEY);
        set.add(MODULES_LIST);
        return set;
    }
}
