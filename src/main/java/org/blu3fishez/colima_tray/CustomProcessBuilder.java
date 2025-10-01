package org.blu3fishez.colima_tray;

import java.util.Map;

/**
 * 싱글톤으로 써야했던 이유
 * -> static으로 활용하기에는 제한적이거나 코드를 작성하기 불편하게 되지만, 여러개의 인스턴스로 관리하면 비효율적이기 때문
 * 1. 필드 변수들을 초기화하는데 있어서 플로우가 필요한 상황이고, 이를 static 으로 처리하기에는 각 필드변수들에 대한 null 체크 과정이 필요하게 됨
 * 2. 여러개의 인스턴스를 굳이 생성할 필요가 없는 클래스임.
 */
public class CustomProcessBuilder {
    ProcessBuilder pb;
    Map<String, String> env;
    private static final String HOMEBREW_PATH = "/opt/homebrew/bin";
    private static final String DOCKER_PATH = "/usr/local/bin/";
    private static CustomProcessBuilder instance;

    public static CustomProcessBuilder getInstance() {
        if (instance == null) {
            instance = new CustomProcessBuilder();
        }
        return instance;
    }

    private CustomProcessBuilder() {
        this.pb = new ProcessBuilder();
        this.env = pb.environment();
        setupEnv();
    }

    private void setupEnv() {
        env.put("PATH", HOMEBREW_PATH + ":" + DOCKER_PATH + ":" + env.get("PATH"));
    }

    public ProcessBuilder getCommand(String... command) {
        return pb.command(command);
    }
}
