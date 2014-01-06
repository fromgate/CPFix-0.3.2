CPFix
=====

CPFix is created to fix some code page problems, bounded to Windows console and broken version of LWJGL included to Minecraft Client. It create to resolve Russian (Cyrillic) issues, but you can easy configure this plugin to any code page and characters set (broken by LWJGL).

Features
--------
* Fix characters in chat and in in-game commands
* Fix characters at signs. Including signs, that was created before CPFix installation (you just need to click sign with wrong codepage and it will be fixed)
* Fix characters at books (Author, Title, Pages)
* Fix characters at items' names and lore (disabled by default)
* Inform player when he typing wrong character in chat or at sign
* Recode output (console, server log) and input - very useful for Windows users.

![example](http://dev.bukkit.org/media/images/52/700/CPFix.gif "Example")

Why do I want it?
-----------------

If your players sometimes typing at chat or at signs using Russian language this plugin is required to provide players full compatibility with UTF-8.

Commands:
---------

* /cpfix — Show current configuration
* /cpfix reload — Reloads configuration

Configuration
-------------

To configure plugin, you must edit config.yml file and type /cpfix reload to reload configuration from the file:

> general:
> \# Find new of plugin version at BukkitDev
>   check-updates: true
> \# Language (russian - is default)
>   language: russian
> \# Save current language to a file
>   language-save: false
> code-page:
> \# Enable (true) recoding chat message
>   chat-fix-enable: true
> \# Enable (true) recoding commands
>   command-fix-enable: true
> \# Enable (true) recoding signs
>   sign-fix-enable: true
> \# Enable (true) recoding books
>   book-fix-enable: true
> \# Enable (true) recoding item's name and lore (Disabled by default)
>   lore-fix-enable: false
> \# Inform player if he types wrong character at chat or at sign (only once :))
>   inform-player: true
> \# Recode output console and server.log
> output-recode:
>   console:
>     enable: true
>     code-page: CP866
>   server-log:
>     enable: true
>     code-page: CP866
> \# Recode input (console only)
> input-recode:
>   enable: true
>   code-page: CP866

character.txt file includes charsets containing "wrong" character set and "right" characters set. If typed message contains any wrong symbol (character located at the first line) it will be replaced with character located in "right" character set (second line) in same position. If you don't need to correct Russian symbols, but you going to replace any symbol with another - you use this plugin to do it. Just configure this characters sets. Default character.txt:

> ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ¸¨
> АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяёЁ

Permissions
-----------

* cpfix.sign - Player with this permission can click signs to fix text located on it
* cpfix.config - Allows to use commands and receive plugin update information

Metrics and update checker
--------------------------

CPFix includes two features that use your server internet connection. First one is Metrics, using to collect information about plugin (versions of plugin, of Java.. etc.) and second is update checker (required to find newer version of CPFix at dev.bukkit.org). If you don't like this features you can easy disable it. To disable update checker you need to set parameter "version-check" to "false" in config.yml. Obtain more information about Metrics and learn how to switch off it, you can read [here](http://mcstats.org/learn-more/).
