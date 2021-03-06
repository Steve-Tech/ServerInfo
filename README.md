# ServerInfo
A simple and lightweight info plugin that supports a Markdown similar syntax for links and commands, as well as PlaceholderAPI placeholders.
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
show-hover: true # Enables a hover message that shows what will happen when clicked
hover-color: YELLOW # The colour of the hover message
refresh-on-send: false # This will refresh the message everytime its send, useful for dynamic papi placeholders
show-on-join: true # Shows the message on join
first-join-only: false # Only show on the first join
```
## Commands
- Info:
  - Description: Shows info about the server
  - Usage: /\<command\>
  - Permission: ServerInfo.info
  - Default: true
- ServerInfo:
  - Description: Reload the ServerInfo plugin
  - Usage: /\<command\> [reload]
  - Permission: ServerInfo.reload
  - Default: op
