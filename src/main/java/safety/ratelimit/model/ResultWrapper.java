//package safety.ratelimit.model;
//
//import com.msb.framework.common.exception.BaseResultCodeEnum;
//import com.msb.framework.common.exception.IResultCode;
//import io.swagger.annotations.ApiModelProperty;
//import org.apache.skywalking.apm.toolkit.trace.Trace;
//import org.apache.skywalking.apm.toolkit.trace.TraceContext;
//
///**
// * 响应结果包装类
// *
// * @author liao, R
// */
//@Setter
//@Getter
//@ToString
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ResultWrapper<T> {
////    @ApiModelProperty("结果码")
//    private String code;
////    @ApiModelProperty("消息")
//    private String message;
////    @ApiModelProperty("链路id")
//    private String traceId;
////    @ApiModelProperty("返回数据内容")
//    private T data;
//
//
//    public ResultWrapper(String code, String message) {
//        this.code = code;
//        this.message = message;
//        this.setTraceId();
//    }
//
//    public ResultWrapper(IResultCode resultCode) {
//        this(resultCode.getCode(), resultCode.getMessage());
//    }
//
//    public ResultWrapper(IResultCode resultCode, T data) {
//        this(resultCode);
//        this.data = data;
//    }
//
//
//    public static <T> ResultWrapper<T> success(T data) {
//        return new ResultWrapper<>(BaseResultCodeEnum.SUCCESS, data);
//    }
//
//    public static <T> ResultWrapper<T> success() {
//        return new ResultWrapper<>(BaseResultCodeEnum.SUCCESS);
//    }
//
//
//    public static ResultWrapper<Void> fail(IResultCode resultCode, String detailErrorMsg) {
//        return new ResultWrapper<>(resultCode.getCode(), resultCode.getMessage() + "：" + detailErrorMsg);
//    }
//
//    public static <T> ResultWrapper<T> fail(IResultCode resultCode) {
//        return new ResultWrapper<>(resultCode.getCode(), resultCode.getMessage());
//    }
//
//    public static <T> ResultWrapper<T> fail(String code, String message) {
//        return new ResultWrapper<>(code, message);
//    }
//
//    @Trace
//    public void setTraceId() {
//        this.traceId = TraceContext.traceId();
//    }
//
//}
