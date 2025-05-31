package jrainroot.still_home.global.ResultData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@AllArgsConstructor
public class ResultData<T> {
    private String resultCode;
    private String msg;
    private T data;

    public static <T> ResultData<T> of(String resultCode, String msg, T data) {
        return new ResultData<T>(resultCode, msg, data);
    }

    public static <T> ResultData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return resultCode.startsWith("S-");
//        return resultCode.equals("200");
    }

    @JsonIgnore
    public boolean isError() {
        return !isSuccess();
    }
}
