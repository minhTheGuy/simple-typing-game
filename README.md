# Simple Typing Game

# Simple Typing Game

A modern typing speed game built with Vue.js 3 + TypeScript frontend and Spring Boot backend with OAuth2 authentication. Test your typing skills, improve your WPM (Words Per Minute), track your accuracy in real-time, and save your progress with user profiles!

## 🎮 Features

### Core Game Features
- **Real-time Typing Test**: Interactive typing interface with live character highlighting
- **Performance Metrics**: Track WPM (Words Per Minute) and accuracy percentage
- **Timer System**: 60-second countdown timer
- **Visual Feedback**: Color-coded character feedback (green for correct, red for incorrect)
- **Multiple Text Samples**: Random selection from various text passages
- **Game Results**: Final score display after completion

### Authentication & User Management
- **OAuth2 Integration**: Sign in with Google and GitHub
- **Traditional Login**: Username/password authentication
- **User Registration**: Create local accounts
- **Session Management**: Persistent login sessions
- **User Profiles**: Comprehensive user information and statistics
- **Account Settings**: Manage profile information

### UI/UX Features
- **Responsive Design**: Clean, modern UI built with Tailwind CSS
- **User Dashboard**: Welcome messages and personalized experience
- **Navigation System**: Intuitive routing with Vue Router
- **Real-time Updates**: Reactive state management across components

## 🛠️ Tech Stack

### Frontend
- **Vue.js 3** - Progressive JavaScript framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Fast build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **Vue Router** - Client-side routing
- **Axios** - HTTP client for API requests
- **Composition API** - Modern Vue.js development approach

### Backend
- **Spring Boot** - Java-based framework
- **Spring Web** - REST API development
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication and authorization
- **Spring OAuth2** - OAuth2 integration with Google and GitHub
- **H2 Database** - In-memory database for development
- **Lombok** - Java annotation library
- **Maven** - Dependency management and build tool

## 📁 Project Structure

```
simple-typing-game/
├── frontend/                    # Vue.js application
│   ├── src/
│   │   ├── components/          # Vue components
│   │   │   ├── GameInterface.vue
│   │   │   ├── Navbar.vue
│   │   │   ├── UserInfoWidget.vue
│   │   │   ├── UserProfile.vue
│   │   │   └── UserStats.vue
│   │   ├── composables/         # Vue composables
│   │   │   ├── useAuth.ts
│   │   │   ├── useApi.ts
│   │   │   └── useUser.ts
│   │   ├── view/                # Page components
│   │   │   ├── Home.vue
│   │   │   ├── Game.vue
│   │   │   └── Profile.vue
│   │   ├── router/              # Vue Router configuration
│   │   ├── types/               # TypeScript type definitions
│   │   ├── App.vue              # Main component
│   │   └── main.ts              # Application entry point
│   ├── package.json
│   └── vite.config.js
├── backend/                     # Spring Boot application
│   ├── src/main/java/com/minh/simple_typing_game/
│   │   ├── controller/          # REST controllers
│   │   │   ├── AuthController.java
│   │   │   └── UserController.java
│   │   ├── service/             # Business logic
│   │   │   ├── AuthService.java
│   │   │   └── UserService.java
│   │   ├── entity/              # JPA entities
│   │   │   └── User.java
│   │   ├── repository/          # Data repositories
│   │   │   └── UserRepository.java
│   │   ├── security/            # Security configuration
│   │   │   └── WebSecurityConfig.java
│   │   └── config/              # Application configuration
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Node.js (v16 or higher)
- pnpm (recommended) or npm
- Java 17+ (for backend development)

### Frontend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/minhTheGuy/simple-typing-game.git
   cd simple-typing-game
   ```

2. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

3. **Install dependencies**
   ```bash
   npm install
   ```

4. **Start development server**
   ```bash
   npm run dev
   ```

5. **Open your browser**
   ```
   http://localhost:5173
   ```

### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Run the Spring Boot application**
   ```bash
   mvnw spring-boot:run
   ```

3. **Backend will be available at**
   ```
   http://localhost:8080
   ```

### OAuth2 Configuration

To enable OAuth2 authentication, you need to configure your Google and GitHub OAuth applications:

1. **Google OAuth2 Setup**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one
   - Enable Google+ API
   - Create OAuth 2.0 credentials
   - Add `http://localhost:8080/oauth2/authorization/google` to redirect URIs

2. **GitHub OAuth2 Setup**:
   - Go to GitHub Settings > Developer settings > OAuth Apps
   - Create a new OAuth App
   - Set Authorization callback URL to `http://localhost:8080/oauth2/authorization/github`

3. **Update application.properties**:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=your-google-client-id
   spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
   spring.security.oauth2.client.registration.github.client-id=your-github-client-id
   spring.security.oauth2.client.registration.github.client-secret=your-github-client-secret
   ```

## 🎯 How to Play

1. **Sign In** (Optional): Use Google/GitHub OAuth or create a local account to save your progress
2. Click **"Start Game"** to begin
3. Type the displayed text in the textarea below
4. Watch your **WPM** and **Accuracy** update in real-time
5. Characters turn:
   - 🟢 **Green** - Correctly typed
   - 🔴 **Red** - Incorrectly typed
6. Complete the text or wait for the 60-second timer
7. View your final results and track your improvement!

## 📊 Game Mechanics

- **WPM Calculation**: `(Total Words Typed / Minutes Elapsed)`
- **Accuracy Calculation**: `(Correct Characters / Total Characters) × 100`
- **Timer**: 60 seconds countdown
- **Text Samples**: Randomly selected from predefined passages

## 🔧 Development

### Available Scripts (Frontend)

```bash
# Development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Type checking
npm run type-check
```

### Key Vue.js Concepts Used

- **Composition API** with `<script setup>`
- **Reactive References** (`ref()`)
- **Computed Properties** for derived state
- **Template Directives** (`v-for`, `v-if`, `v-model`)
- **Event Handling** (`@click`, `@input`)
- **Lifecycle Hooks** (`onMounted`)

## 🎨 UI Components

- **Game Header**: Title and branding
- **Stats Bar**: Real-time WPM, accuracy, and timer
- **Text Display**: Character-by-character feedback
- **Input Area**: Textarea for user typing
- **Control Buttons**: Start and reset functionality
- **Results Panel**: Final score display
- **User Navigation**: Authentication and profile management
- **Footer**: Developer credits

## 📈 Features Status

- [x] **User Authentication**: Complete OAuth2 and local authentication
- [x] **User Profiles**: User information and statistics display
- [x] **Game Functionality**: Full typing game with real-time feedback
- [x] **Session Management**: Persistent login sessions
- [x] **Responsive Design**: Mobile-friendly interface
- [ ] Score history and statistics persistence
- [ ] Multiplayer typing races
- [ ] Custom text upload
- [ ] Difficulty levels
- [ ] Leaderboards
- [ ] Practice mode with specific word sets
- [ ] Dark/light theme toggle

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🐛 Known Issues

- OAuth2 logout functionality needs improvement
- Profile page routing guard needs authentication check
- Timer continues if user completes text early (will be fixed)
- WPM calculation could be more sophisticated
- Need better mobile responsiveness

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Minh**
- GitHub: [@minhTheGuy](https://github.com/minhTheGuy)

## 🙏 Acknowledgments

- Vue.js team for the amazing framework
- Tailwind CSS for the utility-first approach
- Spring Boot community for backend inspiration

---

### 📞 Support

If you have any questions or run into issues, please open an issue on GitHub or contact the developer.

**Happy Typing! 🚀**
