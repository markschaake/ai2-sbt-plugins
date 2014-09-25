[![Build Status](https://magnum.travis-ci.com/allenai/sbt-plugins.svg?token=bTo69ep8z4cnh7oxWjjY)](https://magnum.travis-ci.com/allenai/sbt-plugins)

SBT Plugins
===========

**Boss**: Mark S

## Publishing

Following the Typesafe lead, we publish our plugins to a [bintray](https://bintray.com/allenai/sbt-plugins).

Bintray does not like snapshot versions, so all of our published versions are releases.

To publish new versions you will need a `~/.bintray/.credentials` file with the following contents:

```
realm = Bintray API Realm
host = api.bintray.com
user = ai2-dev
password = [API Key for ai2-dev user]
```

## Developing Plugins

Each plugin subproject also has a `[subproj]-tester` directory, which is a separate SBT project using the plugin under test. These projects are meant for rapid feedback while making changes to the plugins, and not necessarily as a template for how to use the plugins.
