package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.DataSink;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.TransformFuture;
import com.koushikdutta.async.parser.AsyncParser;
import com.koushikdutta.async.parser.ByteBufferListParser;
import com.koushikdutta.async.stream.ByteBufferListInputStream;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant.HttpTaskResult;

/**
 * HttpTaskResult 解析器
 * Created by wangzf on 2015/8/17.
 */
public class HttpTaskResultParser implements AsyncParser<HttpTaskResult> {

    Charset forcedCharset;

    public HttpTaskResultParser() {
    }

    public HttpTaskResultParser(Charset forcedCharset) {
        this.forcedCharset = forcedCharset;
    }

    @Override
    public Future<HttpTaskResult> parse(DataEmitter emitter) {
        final String charset = emitter.charset();
        return new ByteBufferListParser().parse(emitter)
                .then(new TransformFuture<HttpTaskResult, ByteBufferList>() {
                    @Override
                    protected void transform(ByteBufferList result) throws Exception {

//                        Charset charsetToUse = forcedCharset;
//                        if (charsetToUse == null && charset != null)
//                            charsetToUse = Charset.forName(charset);
//                        // 解析返回数据
//                        String resultStr = result.readString(charsetToUse);
//                        HttpTaskResult taskResult = new HttpTaskResult();
//                        taskResult.parseJson(resultStr);

                        JsonParser parser = new JsonParser();
                        ByteBufferListInputStream bis = new ByteBufferListInputStream(result);
                        InputStreamReader isr;
                        if (forcedCharset != null)
                            isr = new InputStreamReader(bis, forcedCharset);
                        else if (charset != null)
                            isr = new InputStreamReader(bis, charset);
                        else
                            isr = new InputStreamReader(bis);
                        JsonElement parsed = parser.parse(new JsonReader(isr));
                        if (parsed.isJsonNull() || parsed.isJsonPrimitive())
                            throw new JsonParseException("unable to parse json");
                        HttpTaskResult taskResult = new HttpTaskResult();
                        taskResult.parse(parsed.getAsJsonObject());
                        setComplete(null, taskResult);
                    }
                });
    }

    @Override
    public void write(DataSink sink, HttpTaskResult value, CompletedCallback completed) {
        new ByteBufferListParser().write(sink, new ByteBufferList(value.toString().getBytes()), completed);
    }

    @Override
    public Type getType() {
        return HttpTaskResult.class;
    }
}
