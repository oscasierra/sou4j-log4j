# sou4j-log4j

sou4j-log4j は、プロジェクト「創 (Sou)」のサブプロジェクトです。 sou4j-log4j では、ログライブラリ log4j 用のAppenderクラスを提供します。


## 本プロジェクトが提供するAppenderクラス
###SlackIncomingWebHooksAppender
ログを Slack の IncomingWebHooks に通知する Appender です。 Appender の設定上は全てのレベルのログを Slack に通知することができますが、Fatal や Error のレベルのログを Slack に通知するのに利用するのが良いでしょう。


## 依存ライブラリ
* log4j
* [google-gson](https://github.com/google/gson "google-gson") : Slack への JSON リクエストに利用しています。


## インストール方法
プロジェクトには Apache Maven の pom.xml を含みますが、現時点では Maven Apache の Central Repository などの公開リポジトリに登録していません。 下記のようにコンパイルやインストールしてご利用ください。

1. プロジェクトをクローンする

		$ git clone https://github.com/oscasierra/sou4j-log4j.git

2. ローカルリポジトリにインストールする

		$ mvn install


## 設定値
### SlackIncomingWebHooksAppender

| 項目              | 説明 |
|------------------+------|
| webHookURL (必須) | Slack の IncomingWebHooks の URL を設定します。 |
| username         | Slack に通知をする際のユーザー名を設定します。 デフォルトは IncomingWebHooks の URL を取得する Slack 画面で入力したユーザー名です。 ユーザー名を変更したい場合に設定します。 |
| fatalIconEmoji   | FATAL レベルの通知をする際の Slack 絵文字を設定します。 デフォルトは　:exclamation:　です。 (※1) |
| errorIconEmoji   | ERROR レベルの通知をする際の Slack 絵文字を設定します。 デフォルトは :exclamation: です。 (※1) |
| warnIconEmoji    | WARN レベルの通知をする際の Slack 絵文字を設定します。 デフォルトは :warning: です。 (※1) |
| infoIconEmoji    | INFO レベルの通知をする際の Slack 絵文字を設定します。 デフォルトは :bell: です。 (※1) |
| debugIconEmoji   | DEBUG レベルの通知をする際の Slack 絵文字を設定します。　デフォルトは :bell: です。 (※1) |
| traceIconEmoji   | TRACE レベルの通知をする際の Slack 絵文字を設定します。 デフォルトは :bell: です。 (※1) |
| channel          | 通知先のチャネルや相手を設定します。 デフォルトは IncomingWebHooks の URL を取得する Slack の画面で入力した通知先です。 通知先を変更したい場合に設定します。 |
| iconURL          | 通知の際のアイコンを画像にしたい場合に URL を設定できます。 この URL を設定した場合、上記の xxxxEmoji の設定値を設定していても、この URL がの画像が優先されます。 |

※1. 利用できる絵文字のコードは [EMOJI CHART SHEET](http://www.emoji-cheat-sheet.com/) などのページを参考にしてください。

## 設定ファイルの例
### log4j.properties

	log4j.rootCategory=TRACE,SLACK

	log4j.appender.SLACK=jp.sou4j.log4j.appender.SlackIncomingWebHooksAppender
	log4j.appender.SLACK.Threshold=ERROR
	log4j.appender.SLACK.layout=org.apache.log4j.PatternLayout
	log4j.appender.SLACK.layout.ConversionPattern=%-5p %d [%t] %m%n
	log4j.appender.SLACK.webHookURL=https://hooks.slack.com/services/XXXXXXXXX/YYYYYYYYY/ZZZZZZZZZZZZZZZZZZZZZZZZ
	log4j.appender.SLACK.username=MyApplication
	log4j.appender.SLACK.fatalIconEmoji=:exclamation:
	log4j.appender.SLACK.errorIconEmoji=:exclamation:
	log4j.appender.SLACK.warnIconEmoji=:warning:
	log4j.appender.SLACK.infoIconEmoji=:bell:
	log4j.appender.SLACK.debugIconEmoji=:bell:
	log4j.appender.SLACK.traceIconEmoji=:bell:
	log4j.appender.SLACK.channel=#notice
	#log4j.appender.SLACK.iconURL=https://hogemoge.com/icon.jpg

