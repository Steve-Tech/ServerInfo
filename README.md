# ServerInfo
A simple and lightweight info plugin.
## Config
```yaml
# Config File for Steve's ServerInfo Plugin
# Markdown similar syntax for links and commands supported, eg.
# - "[Click for help](COMMAND /help) runs /help"
# - "[Click for help](SUGGEST /help <plugin>) puts /help into the textbox"
# - "[Click for code](COPY code) copies text to the clipboard"
# - "[Click for website](LINK http://example.com) opens link"
message:
  - ''
  - '&6----------------------------------------------------------------'
  - ''
  - '&2Welcome to the server'
  - '&aTo view the rules type [&n/rules](COMMAND /rules)'
  - ''
  - '&6----------------------------------------------------------------'
  - ''
show-hover: true
hover-color: YELLOW
show-on-join: true
first-join-only: false
```
## Commands
- Info:
  - Description: Shows info about the server
  - Usage: /<command>
  - Permission: ServerInfo.info
  - Default: true
- ServerInfo:
  - Description: Reload the ServerInfo plugin
  - Usage: /<command> [reload]
  - Permission: ServerInfo.reload
  - Default: op
