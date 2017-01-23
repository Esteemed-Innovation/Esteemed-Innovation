# Contributing
We love when people help us do our job for us. Here is some information on how you can do that.

## Issues
Creating issues is the most basic way to help us develop the mod. Issues can be created for various reasons, for example: you found a bug that should be reported or you have a feature idea.

According labels will be added to your issues as soon as an active contributor sees it (typically @elifoster or @xbony2).

### Bug reporting
Bug reports should include the Minecraft Forge version, the Flaxbeard's Steam Power version, and a detailed explanation of the bug. This explanation should *generally* include:
- When it happens
- The blocks/items involved
- Any extra mods that may be interfering

### Feature suggesting
Feature suggestions are pretty loose. They should be as detailed as the feature is complicated.

## Code contributions
Pull requests can be made to submit code snippets to the mod to either fix bugs, add features, or just generally improve it. Pull requests should always match the style guide (below), and ones that do not will be rejected.

### Getting started
The setup of the project is mildly complex. Clone the repository, and follow the steps in the [MC Forge docs](http://mcforge.readthedocs.io/en/latest/gettingstarted/).

If it is not already set up in such a way by default, be sure that the following source/resource roots are linked up to the correct modules:
* `src/api/java` and `src/api/resources`: Esteemed Innovation api
* `src/main/java` and `src/main/resources`: Esteemed Innovation main
* `src/test/java` and `src/test/resources`: Esteemed Innovation test

Main must be dependent on API, Test must be dependent on Main, and API must be independent of both.

When possible, use tests. This isn't very common because testing with MC and MC Forge is incredibly complicated.

Always be sure that the API module is not dependent on anything within the Main module. If it is set up properly, you should simply be unable to import classes from Main into API. The API package must be able to compile independently of the mod.

#### Package system
This is discussed briefly in the style guide, however it is logical to describe it here too.

This project is packaged using the package-by-feature style, rather than package-by-layer. For example, many mods would put all `TileEntity` classes into a `tile` package. We do not. Instead, we put all classes relating to a single feature in a focused package. The `commons` package is for common code that can be accessed from any package. Feature packages should only access the ContentModules within other feature packages.

#### Commit messages
We're pretty lenient about these guidelines, so it's not a huge deal if it's not followed. However, following it definitely improves the cleanliness of the project, and promotes good style! Try your best to follow this!
* Use the present tense (fix not fixed).
* Use the imperative mood (fix not fixes).
* Limit the first line to 72 characters or less. If you need to provide more information, use the first line for a summary, then describe in great detail in the next lines.
* Mention issues and pull requests liberally, unless closing or "opening" (GitHub does not support re-opening issues in commits, but if you state "Open #5" and then manually open it, it will achieve the same thing).
* Consider prefixing your commits with emojis:
  * :art: `:art:` when dealing with textures, either loading them or adding new ones.
  * :saxophone: `:saxophone:` when dealing with sounds, either loading them or adding new ones.
  * Any of the earths (:earth_africa: :earth_asia: :earth_americas: :globe_with_meridians: `:earth_africa: :earth_asia: :earth_americas: :globe_with_meridians:`) when dealing with localization.
  * :lipstick: `:lipstick:` when changing the cosmetics of code.
  * :package: `:package:` when refactoring.
  * :birthday: `:birthday:` when updating the mod version number.
  * :arrow_up: `:arrow_up:` when upgrading dependency versions.
  * :arrow_down: `:arrow_down:` when downgrading dependency versions.
  * :white_check_mark: `:white_check_mark:` when adding tests.
  * :broken_heart: `:broken_heart:` when deleting code or files (not replacing it).
  * :apple: `:apple:` when dealing with OS X specifically.
  * :penguin: `:penguin:` when dealing with Linux specifically.
  * :checkered_flag: `:checkered_flag:` when dealing with Windows specifically.
  * :racehorse: `:racehorse:` when improving performance.
  * :non-potable_water: `:non-potable_water:` when fixing memory leaks.
  * :book: `:book:` when updating or adding docs.
  * :bug: `:bug:` when fixing a bug.
  * :shirt: `:shirt:` when resolving linter warnings.
  * :gem: `:gem:` when adding a feature.
  * :wrench: `:wrench:` when tweaking a feature.
  * :notebook: `:notebook:` when adding Esteemed Innovation journal entries.
  * :octocat: `:octocat:` for meta commits.

You are not required to use emojis, but it is allowed and encouraged, as it allows for quickly knowing the purpose of a commit.

#### Java Style Guide
Please see [the style guide](STYLE_GUIDE.md)