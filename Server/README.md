# RoboPiHEIG - Server

The source for the server/robot part of the RoboPiHEIG project.

##  Toolchain setup

We use the toolchain provided by the [RPi-Cpp-Toolchain](https://github.com/tttapa/RPi-Cpp-Toolchain) project. It uses docker to retrieve and generate the appropriate toolchain and sysroot for the Raspberry Pi Robot.

To do cross compilation, we need a *toolchain* and a *sysroot*. The toolchain contains the compiler suite for our target architecture. The sysroot is a minimal copy of the target filesystem.

1. Clone the RPi-Cpp-Toolchain :

```sh
git clone git@github.com:tttapa/RPi-Cpp-Toolchain.git
```

2. In the `toolchain` directory, run `./toolchain.sh rpi3-armv8-dev --pull --export`. The `rpi3-armv8-dev` is suitable for the Raspberry Pi 3 with a 32-bit raspbian OS. When the script is done, the `x-tools` directory contains the toolchain. You will need docker for this step.

3. Install the toolchain into your `/opt` directory :

```sh
cp x-tools /opt -r
```
