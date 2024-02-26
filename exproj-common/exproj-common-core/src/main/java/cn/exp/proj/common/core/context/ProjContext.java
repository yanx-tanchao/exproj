package cn.exp.proj.common.core.context;

public class ProjContext {
    private static final ThreadLocal<String> PAYLOAD_HOLDER = new ThreadLocal<>();

    public static void clear() {
        reset();
    }

    public static void reset() {
        PAYLOAD_HOLDER.remove();
    }

}
