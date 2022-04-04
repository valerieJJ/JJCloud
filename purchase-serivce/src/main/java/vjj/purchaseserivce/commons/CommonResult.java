package vjj.purchaseserivce.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;
//    private Object data;

    public CommonResult(Integer code,String message){
        this(code,message,null);
    }

//    public CommonResult(Integer code, String message, T o) {
//        this.code = code;
//        this.message = message;
//        this.data = o;
//    }
}
