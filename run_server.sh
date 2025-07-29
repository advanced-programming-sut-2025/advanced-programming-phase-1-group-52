#!/bin/bash

# Stardew Valley Network Server Launcher
echo "=== Stardew Valley Network Server ==="

# Default values
IP=${1:-"localhost"}
PORT=${2:-8080}

echo "Starting server on $IP:$PORT"
echo "Usage: ./run_server.sh [ip] [port]"
echo ""

# Run the server
java -cp "lwjgl3/build/libs/*" com.example.main.lwjgl3.NetworkServerLauncher "$IP" "$PORT" 