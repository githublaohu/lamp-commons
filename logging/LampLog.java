package com.lamp.commons.lang.logging;

public interface LampLog {


    void error(String msg, Throwable e);

    void error(String msg);


    void info(String msg);

    void debug(String msg);

    void debug(String msg, Throwable e);

    void warn(String msg);

    void warn(String msg, Throwable e);
    
    boolean isInfoEnabled();

    boolean isDebugEnabled();
    
    boolean isWarnEnabled();
    
    boolean isErrorEnabled();

    int getErrorCount();

    int getWarnCount();

    int getInfoCount();

    int getDebugCount();

    void resetStat();

}
