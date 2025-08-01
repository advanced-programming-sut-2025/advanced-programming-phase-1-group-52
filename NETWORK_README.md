# Stardew Valley Network System

This document explains how to use the network multiplayer system for the Stardew Valley game.

## Overview

The network system allows multiple players to connect to a server and play together in a turn-based multiplayer game. The system consists of:

1. **Network Server** - Handles multiple client connections and game state
2. **Network Client** - Connects to the server and provides the game interface
3. **Network Lobby** - Allows players to invite each other and form game sessions

## Quick Start

### 1. Start the Server

First, start the network server in one terminal:

```bash
# Using the script (recommended)
./run_server.sh [ip] [port]

# Examples:
./run_server.sh                    # Start on localhost:8080
./run_server.sh 192.168.1.100     # Start on specific IP
./run_server.sh localhost 9000    # Start on specific port
```

Or run directly with Java:
```bash
java -cp "build/libs/*" com.example.main.lwjgl3.NetworkServerLauncher [ip] [port]
```

### 2. Start Clients

In separate terminals, start the network clients:

```bash
# Using the script (recommended)
./run_client.sh [server_ip] [server_port]

# Examples:
./run_client.sh                    # Connect to localhost:8080
./run_client.sh 192.168.1.100     # Connect to specific server
./run_client.sh localhost 9000    # Connect to specific port
```

Or run directly with Java:
```bash
java -cp "build/libs/*" com.example.main.lwjgl3.NetworkClientLauncher [server_ip] [server_port]
```

## How to Play

### 1. Server Setup
1. Start the server using one of the methods above
2. The server will display connection information
3. Wait for clients to connect

### 2. Client Setup
1. Start a client using one of the methods above
2. The client will automatically connect to the server
3. Log in with your credentials (test users: player1/password1, etc.)
4. You'll be taken to the main menu

### 3. Network Lobby
1. From the main menu, click "Network Lobby"
2. The lobby will show:
   - **Online Users**: Other players connected to the server
   - **Lobby Players**: Players in your current lobby
3. To invite someone:
   - Click on a username in the "Online Users" list
   - Click "Invite Selected User"
4. When you have 4 players in the lobby, click "Start Game"

### 4. Game Play
1. Once the game starts, all players will be taken to the game screen
2. The game will be synchronized across all clients
3. Players take turns making moves
4. All game state is managed by the server

## Test Users

The server comes with these test users pre-configured:

- **Username**: player1, **Password**: password1
- **Username**: player2, **Password**: password2  
- **Username**: player3, **Password**: password3
- **Username**: player4, **Password**: password4

## Network Architecture

### Server Components
- `GameServer` - Main server class handling connections
- `ClientHandler` - Manages individual client connections
- `NetworkServerLauncher` - Command-line server launcher

### Client Components
- `NetworkService` - High-level network service
- `GameClient` - Network client implementation
- `NetworkLobbyController` - Lobby functionality
- `GDXNetworkLobby` - Graphical lobby interface
- `NetworkClientLauncher` - Command-line client launcher

### Message System
- `Message` - Network message class
- `MessageType` - Message type enumeration
- JSON-based communication protocol

## Troubleshooting

### Common Issues

1. **Connection Failed**
   - Check if server is running
   - Verify IP address and port
   - Check firewall settings

2. **Authentication Failed**
   - Use correct test credentials
   - Ensure user exists on server

3. **Graphics Mode Not Working**
   - The client will automatically fall back to terminal mode
   - Check your display environment

### Debug Information

The server and clients provide detailed console output:
- Connection status
- Authentication results
- Message exchanges
- Error information

## Development Notes

### Adding New Message Types
1. Add new enum value to `MessageType`
2. Update server and client handlers
3. Test message flow

### Customizing the Lobby
1. Modify `GDXNetworkLobby` for UI changes
2. Update `NetworkLobbyController` for logic changes
3. Extend server-side lobby management

### Security Considerations
- Current implementation uses simple password authentication
- In production, implement proper password hashing
- Consider adding encryption for sensitive data
- Implement proper session management

## Future Enhancements

1. **Database Integration** - Replace in-memory user storage
2. **Encryption** - Add SSL/TLS for secure communication
3. **Chat System** - Add in-game chat functionality
4. **Spectator Mode** - Allow players to watch games
5. **Replay System** - Record and replay game sessions
6. **Room System** - Support multiple game rooms/sessions 