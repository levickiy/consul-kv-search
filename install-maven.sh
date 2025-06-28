#!/usr/bin/env bash
# Simple helper script to install Maven on Debian/Ubuntu based systems.
# Requires sudo privileges.

set -e

if command -v mvn >/dev/null 2>&1; then
    echo "Maven is already installed"
    exit 0
fi

if command -v apt-get >/dev/null 2>&1; then
    echo "Installing Maven using apt-get";
    sudo apt-get update && sudo apt-get install -y maven
    echo "Maven installed"
else
    echo "Please install Maven using your system's package manager." >&2
    exit 1
fi
