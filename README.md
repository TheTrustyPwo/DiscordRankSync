[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]


<!-- PROJECT TITLE -->
<!--suppress HtmlDeprecatedAttribute, HtmlUnknownAnchorTarget -->

<div align="center">
<h3 align="center">Discord Rank Sync</h3>
  <p align="center">
    Lightweight discord integration to synchronize ranks and reward your donors!
    <br/>
    <a href="https://github.com/TheTrustyPwo/discordjs-bot-template/issues">Report Bug</a>
    ·
    <a href="https://github.com/TheTrustyPwo/discordjs-bot-template/issues">Request Feature</a>
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#installation">Installation</a></li>
    <li><a href="#commands">Commands</a></li>
    <li><a href="#developer-api">Developer API</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>


<!-- ABOUT THE PROJECT -->
## About The Project

Extremely lightweight plugin to synchronize in-game ranks with Discord roles!

Here's why you should use this plugin:
* Lightweight & Optimized
* Easy setup; Drag & Drop (Set up your discord bot, and you are ready to go!)
* Multiple database options: MongoDB
* Developer API in case you want to implement this in your plugins!
* Extremely and easily configurable

<p align="right">(<a href="#top">back to top</a>)</p>


## Installation

This shows how you install the plugin.

1. Create an application at [https://discord.com/developers/applications](https://discord.com/developers/applications)
2. Create a bot within the application and copy the bot token
3. Click `OAuth2` > `URL Generator`
4. Check `applications.commands` & `bot` under `Scopes`
5. Check whichever permissions your bot will require under `Bot Permissions`
6. Copy the `Generated URL` and invite the bot to your discord server
7. Drop this plugin in your `./plugins` folder
8. Once the `config.yml` is created, insert the bot token which you have copied and restart the server
9. Join the server and start linking your account with `/discord link`!

<p align="right">(<a href="#top">back to top</a>)</p>


## Commands
This is a list of commands available in the plugin and their respective permissions required.

| Command | Description | Permission |
| ------- | ----------- | ---------- |
| `/discord` | Displays the Discord server's invite link | None |
| `/discord help` | Displays the command help menu in game | None |
| `/discord link` | Generates a code for you to link with your Discord account | None |
| `/discord unlink` | Unlinks with your Discord account | None |
| `/discord whois <player>` | Displays the target's Discord user information | None |
| `/discord reload` | Reloads the plugin configuration | `drs.reload` |

<p align="right">(<a href="#top">back to top</a>)</p>


## Developer API
This section is for developers who want to make use of this plugin's API.

### API
```java
package net.evilkingdom.discordranksync.api;

import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

public interface DiscordRankSyncAPI {

    /**
     * Returns whether player is linked with a discord user
     *
     * @param player ~ The player to check if its linked
     * @return ~ Returns true if the player is linked with a discord user else false
     */
    boolean isLinked(OfflinePlayer player);

    /**
     * Get the discord ID of a player, if the player is linked
     *
     * @param player ~ The player to query for its discord ID
     * @return ~ Returns the discord ID as a string if the player is linked, else null
     */
    @Nullable
    String getDiscordId(OfflinePlayer player);
}
```

To get the API implementation:
```java
DiscordRankSyncAPI api =  ((DiscordRankSync) Bukkit.getPluginManager().getPlugin("DiscordRankSync")).getInstance().getApi();
```

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

TheTrustyPwo - Pwo#0001 - thetrustypwo@gmail.com

Project Link: [https://github.com/EvilKingdom/DiscordRankSync](https://github.com/EvilKingdom/DiscordRankSync)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/EvilKingdom/DiscordRankSync.svg?style=for-the-badge
[contributors-url]: https://github.com/EvilKingdom/DiscordRankSync/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/EvilKingdom/DiscordRankSync.svg?style=for-the-badge
[forks-url]: https://github.com/EvilKingdom/DiscordRankSync/network/members
[stars-shield]: https://img.shields.io/github/stars/EvilKingdom/DiscordRankSync.svg?style=for-the-badge
[stars-url]: https://github.com/EvilKingdom/DiscordRankSync/stargazers
[issues-shield]: https://img.shields.io/github/issues/EvilKingdom/DiscordRankSync.svg?style=for-the-badge
[issues-url]: https://github.com/EvilKingdom/DiscordRankSync/issues
[license-shield]: https://img.shields.io/github/license/EvilKingdom/DiscordRankSync.svg?style=for-the-badge
[license-url]: https://github.com/EvilKingdom/DiscordRankSync/blob/master/LICENSE.txt