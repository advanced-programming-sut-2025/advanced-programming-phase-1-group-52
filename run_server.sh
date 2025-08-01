#!/bin/bash

# Stardew Valley Network Server Launcher
echo "=== Stardew Valley Network Server ==="

# Default values
IP=${1:-"localhost"}
PORT=${2:-8080}

echo "Starting server on $IP:$PORT"
echo "Usage: ./run_server.sh [ip] [port]"
echo ""

# CORRECTED: Classpath includes core, lwjgl3, AND library dependencies
java -cp "core/build/classes/java/main:lwjgl3/build/classes/java/main:core/build/libs/*" com.example.main.lwjgl3.NetworkServerLauncher "$IP" "$PORT"
