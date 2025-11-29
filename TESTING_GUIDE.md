# Testing Guide - Chatty Application

## Prerequisites

1. **MySQL Database Running**
   - MySQL should be running on `localhost:3306`
   - Default username: `root`, password: (empty)
   - Database will be created automatically if it doesn't exist

2. **Start the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or run `ChattyApplication.java` from your IDE
   - Application will start on: `http://localhost:8080`

---

## Available Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user

### Friends
- `POST /friends/add` - Add friend
- `GET /friends/list` - List friends

### Messages
- `POST /messages/send` - Send message
- `GET /messages/history` - Get chat history

---

## Testing with HTML UI

### Step 1: Access the UI
1. Start the Spring Boot application
2. Open browser and go to: `http://localhost:8080/index.html`

### Step 2: Test Authentication (via Postman first)
Before using the UI, you need to create users using Postman (see Postman section below)

### Step 3: Test Sending Messages
1. Enter **Sender ID** (e.g., `1`)
2. Enter **Receiver ID** (e.g., `2`)
3. Enter **Message Content** (e.g., "Hello!")
4. Click **"Send"** button
5. Message will appear in the messages box below

### Step 4: Test Chat History
1. Enter **User 1 ID** (e.g., `1`)
2. Enter **User 2 ID** (e.g., `2`)
3. Click **"Load Chat History"** button
4. All messages between the two users will be displayed

---

## Testing with Postman

### Setup Postman
1. Install Postman (if not installed)
2. Create a new Collection called "Chatty API"
3. Set base URL: `http://localhost:8081`

---

### 1. Register User

**Request:**
- Method: `POST`
- URL: `http://localhost:8081/auth/register`
- Headers: 
  - `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "username": "alice",
  "password": "password123"
}
```

**Expected Response:**
```
"User successfully registered"
```

**Create 2-3 users for testing:**
- alice / password123
- bob / password123
- charlie / password123

**Note:** After registering, check the database or response to get the user IDs (usually 1, 2, 3, etc.)

---

### 2. Login User

**Request:**
- Method: `POST`
- URL: `http://localhost:8080/auth/login`
- Headers:
  - `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "username": "alice",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "id": 1,
  "username": "alice"
}
```

---

### 3. Add Friend

**Request:**
- Method: `POST`
- URL: `http://localhost:8080/friends/add?userId=1&friendId=2`
- Or use Params tab:
  - userId: `1`
  - friendId: `2`

**Expected Response:**
```
"Friend bob added successfully"
```

**Test:**
- Add friend 2 to user 1
- Add friend 3 to user 1
- Add friend 1 to user 2 (bidirectional)

---

### 4. List Friends

**Request:**
- Method: `GET`
- URL: `http://localhost:8080/friends/list?userId=1`
- Or use Params tab:
  - userId: `1`

**Expected Response:**
```json
[
  {
    "id": 2,
    "username": "bob"
  },
  {
    "id": 3,
    "username": "charlie"
  }
]
```

---

### 5. Send Message

**Request:**
- Method: `POST`
- URL: `http://localhost:8080/messages/send?senderId=1&receiverId=2&content=Hello Bob!`
- Or use Params tab:
  - senderId: `1`
  - receiverId: `2`
  - content: `Hello Bob!`

**Expected Response:**
```json
{
  "id": 1,
  "sender": {
    "id": 1,
    "username": "alice"
  },
  "receiver": {
    "id": 2,
    "username": "bob"
  },
  "content": "Hello Bob!",
  "timestamp": "2025-11-22T12:00:00"
}
```

**Test Multiple Messages:**
- Send message from user 1 to user 2
- Send message from user 2 to user 1
- Send message from user 1 to user 3

---

### 6. Get Chat History

**Request:**
- Method: `GET`
- URL: `http://localhost:8080/messages/history?userId1=1&userId2=2`
- Or use Params tab:
  - userId1: `1`
  - userId2: `2`

**Expected Response:**
```json
[
  {
    "id": 1,
    "sender": {
      "id": 1,
      "username": "alice"
    },
    "receiver": {
      "id": 2,
      "username": "bob"
    },
    "content": "Hello Bob!",
    "timestamp": "2025-11-22T12:00:00"
  },
  {
    "id": 2,
    "sender": {
      "id": 2,
      "username": "bob"
    },
    "receiver": {
      "id": 1,
      "username": "alice"
    },
    "content": "Hi Alice!",
    "timestamp": "2025-11-22T12:01:00"
  }
]
```

---

## Complete Testing Flow

### Step-by-Step Flow:

1. **Register Users** (Postman)
   - Register "alice" → Gets ID 1
   - Register "bob" → Gets ID 2
   - Register "charlie" → Gets ID 3

2. **Login Users** (Postman)
   - Login "alice" → Verify response has ID 1

3. **Add Friends** (Postman)
   - POST `/friends/add?userId=1&friendId=2` → Alice adds Bob
   - POST `/friends/add?userId=1&friendId=3` → Alice adds Charlie

4. **List Friends** (Postman)
   - GET `/friends/list?userId=1` → Should see Bob and Charlie

5. **Send Messages** (Postman or UI)
   - POST `/messages/send?senderId=1&receiverId=2&content=Hello Bob!`
   - POST `/messages/send?senderId=2&receiverId=1&content=Hi Alice!`

6. **Get Chat History** (UI or Postman)
   - GET `/messages/history?userId1=1&userId2=2` → See conversation between Alice and Bob

---

## Troubleshooting

### Issue: Port 8080 already in use
**Solution:** 
- Kill the process: `lsof -ti:8080 | xargs kill -9`
- Or change port in `application.properties`: `server.port=8081`

### Issue: MySQL connection error
**Solution:**
- Make sure MySQL is running: `mysql.server start` (Mac) or `sudo service mysql start` (Linux)
- Check username/password in `application.properties`
- Verify MySQL is on port 3306

### Issue: CORS errors in browser
**Solution:**
- All endpoints are set to `permitAll()` in SecurityConfig, so CORS shouldn't be an issue
- If you still get CORS errors, check browser console

### Issue: 404 Not Found
**Solution:**
- Make sure application is running
- Check the URL (should be `http://localhost:8080/...`)
- Verify endpoint path matches exactly (case-sensitive)

### Issue: HTML UI not loading
**Solution:**
- Access via: `http://localhost:8080/index.html`
- Check browser console for errors
- Verify static files are in `src/main/resources/static/`

---

## Quick Start Commands

```bash
# Start MySQL (if not running)
# Mac:
mysql.server start

# Linux:
sudo service mysql start

# Start Spring Boot application
mvn spring-boot:run

# Or from IDE: Run ChattyApplication.java
```

---

## Testing Checklist

- [ ] Application starts successfully
- [ ] Register user via Postman
- [ ] Login user via Postman
- [ ] Add friend via Postman
- [ ] List friends via Postman
- [ ] Send message via Postman
- [ ] Get chat history via Postman
- [ ] Access HTML UI at http://localhost:8080/index.html
- [ ] Send message via HTML UI
- [ ] Load chat history via HTML UI

