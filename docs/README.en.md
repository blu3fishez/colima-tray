[English](./docs/README.en.md)

# A Tray Application for [colima](https://github.com/abiosoft/colima)

> [!note] Usage Notes
>
> This version is still a prototype and requires feedback from many users.
>
> Currently, only the following limited features are supported:
>
> 1. Does not support `colima`'s profile functionality.
> 2. Memory allocation cannot be adjusted. When toggled, memory allocation is fixed at `4GB`.
>

## Overview

I unexpectedly needed to install Oracle XE on my Mac.

During this process, I discovered `colima`, a Docker context ideal for running x86-based Docker images, and was using it
effectively.

However, one day I noticed my MacBook's battery drain had become excessive. Upon investigation, I found that `colima`,
being a `cli`-based program, was consuming a high level of battery power.

So I devised a way to toggle the `colima` context conveniently while minimizing system burden, deciding to build it as a
JavaFX program.

I chose Java over AppleScript because of its learning curve, opting for the most convenient and least system-intensive
option.

During development, I decided not to use JavaFX's GUI features either, ultimately implementing the app using only
minimal `AWT` functionality.

## Key Features

### 1. Check Colima Status

```
colima status
```

This command automatically runs every 5 seconds to check the `colima` status on my computer.

- active : Colima context is enabled
- inactive : Colima context is disabled
- toggling : Currently toggling the context on/off via the `toggle colima` button in the tray
- error : Unable to check the context due to an error for some reason

## Installation

There are two methods.

### 1. Gradle jpackage Build

You can build it directly by running the jpackage command within Gradle.

### 2. Download from Github Release

Go to the release page on GitHub and download the `.zip` file directly.

After that, unzip the `.zip` file and register the `.app`.

## Contribute

Send me any PR! It's a huge help.

I actually use this project myself.

Therefore, the code will be reviewed thoroughly.

I enjoy communicating through code.
As long as the code and PR rationale are solid, any PR is welcome.
This could be an opportunity to grow alongside this beginner developer.