package xrc.service.ex;

/*密码错误异常*/
public class PwdNotMatchException extends ServiceException{
    public PwdNotMatchException() {
    }

    public PwdNotMatchException(String message) {
        super(message);
    }

    public PwdNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PwdNotMatchException(Throwable cause) {
        super(cause);
    }

    public PwdNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
