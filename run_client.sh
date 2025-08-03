#!/bin/bash

# Stardew Valley Network Client Launcher
echo "=== Stardew Valley Network Client ==="

# Default values
SERVER_IP=${1:-"localhost"}
SERVER_PORT=${2:-8080}

echo "Connecting to server: $SERVER_IP:$SERVER_PORT"
echo "Usage: ./run_client.sh [server_ip] [server_port]"
echo ""

# Run the client
java -cp "lwjgl3/build/libs/*" com.example.main.lwjgl3.NetworkClientLauncher "$SERVER_IP" "$SERVER_PORT" 