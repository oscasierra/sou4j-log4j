package jp.sou4j.log4j.appender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.google.gson.Gson;

/**
 * ログをSlackの Incoming WebHooks に通知する Appender クラスです。
 */
public class SlackIncomingWebHooksAppender extends AppenderSkeleton {

    private static final String FATAL_ICON_EMOJI = ":exclamation:";
    private static final String ERROR_ICON_EMOJI = ":exclamation:";
    private static final String WARN_ICON_EMOJI = ":warning:";
    private static final String INFO_ICON_EMOJI = ":bell:";
    private static final String DEBUG_ICON_EMOJI = ":bell:";
    private static final String TRACE_ICON_EMOJI = ":bell:";

    private String username;
    private String webHookURL;
    private String fatalIconEmoji;
    private String errorIconEmoji;
    private String warnIconEmoji;
    private String infoIconEmoji;
    private String debugIconEmoji;
    private String traceIconEmoji;
    private String iconURL;
    private String channel;

    public SlackIncomingWebHooksAppender() {
        super(true);
    }

    public SlackIncomingWebHooksAppender(Layout layout) {
        super(true);
        super.setLayout(layout);
        super.activateOptions();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWebHookURL(String url) {
        this.webHookURL = url;
    }

    public void setFatalIconEmoji(String emoji) {
        this.fatalIconEmoji = emoji;
    }

    public void setErrorIconEmoji(String emoji) {
        this.errorIconEmoji = emoji;
    }

    public void setWarnIconEmoji(String emoji) {
        this.warnIconEmoji = emoji;
    }

    public void setInfoIconEmoji(String emoji) {
        this.infoIconEmoji = emoji;
    }

    public void setDebugIconEmoji(String emoji) {
        this.debugIconEmoji = emoji;
    }

    public void setTraceIconEmoji(String emoji) {
        this.traceIconEmoji = emoji;
    }

    public void setIconURL(String url) {
        this.iconURL = url;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresLayout() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(final LoggingEvent event) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("text", super.getLayout().format(event));
        if( this.isNullOrEmpty(this.iconURL) ) {
            parameters.put("icon_emoji", this.decideIconEmoji(event));
        }
        if( !this.isNullOrEmpty(this.username) ) {
            parameters.put("username", this.username);
        }
        if( !this.isNullOrEmpty(this.iconURL) ) {
            parameters.put("icon_url", this.iconURL);
        }
        if( !this.isNullOrEmpty(this.channel) ) {
            parameters.put("channel", this.channel);
        }

        try {
            this.sendMessage(parameters);
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 通知に利用する絵文字を決定します。
     * @param event ログイベント
     * @return 決定した絵文字
     */
    private String decideIconEmoji(final LoggingEvent event) {
        String emoji;
        if( event.getLevel() == Level.FATAL ) {
            emoji = (this.isNullOrEmpty(this.fatalIconEmoji)) ? FATAL_ICON_EMOJI : this.fatalIconEmoji ;
        }
        else if( event.getLevel() == Level.ERROR ) {
            emoji = (this.isNullOrEmpty(this.errorIconEmoji)) ? ERROR_ICON_EMOJI : this.errorIconEmoji ;
        }
        else if( event.getLevel() == Level.WARN ) {
            emoji = (this.isNullOrEmpty(this.warnIconEmoji)) ? WARN_ICON_EMOJI : this.warnIconEmoji ;
        }
        else if( event.getLevel() == Level.INFO ) {
            emoji = (this.isNullOrEmpty(this.infoIconEmoji)) ? INFO_ICON_EMOJI : this.infoIconEmoji ;
        }
        else if( event.getLevel() == Level.DEBUG ) {
            emoji = (this.isNullOrEmpty(this.debugIconEmoji)) ? DEBUG_ICON_EMOJI : this.debugIconEmoji ;
        }
        else if( event.getLevel() == Level.TRACE ) {
            emoji = (this.isNullOrEmpty(this.debugIconEmoji)) ? TRACE_ICON_EMOJI : this.traceIconEmoji ;
        }
        else {
            emoji = FATAL_ICON_EMOJI;
        }
        return emoji;
    }

    /**
     * メッセージを送信します
     * @param parameters
     * @throws IOException
     */
    private void sendMessage(final Map<String, String> parameters) throws IOException {
        // 送信するPOSTデータを作成します
        String json = new Gson().toJson(parameters);
        String data = "payload="+json;

        // URLConnectionを生成します
        URL url = new URL(this.webHookURL);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        // リクエスト送信
        try(PrintStream printStream = new PrintStream(connection.getOutputStream())) {
            printStream.print(data);
        }

        // レスポンス取得
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            while(reader.readLine() != null) {}
        }
    }

    /**
     * 指定した文字列が null もしくは空文字である場合に true を返却します。
     * @param target 検証対象文字列
     * @return 検証結果
     */
    private boolean isNullOrEmpty(final String target) {
        return ( target == null || target.equals("") );
    }
}
