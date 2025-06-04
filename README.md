# Simple Typing Game

A modern typing speed game built with Vue.js 3 + TypeScript frontend and Spring Boot backend. Test your typing skills, improve your WPM (Words Per Minute), and track your accuracy in real-time!

## 🎮 Features

- **Real-time Typing Test**: Interactive typing interface with live character highlighting
- **Performance Metrics**: Track WPM (Words Per Minute) and accuracy percentage
- **Timer System**: 60-second countdown timer
- **Visual Feedback**: Color-coded character feedback (green for correct, red for incorrect)
- **Multiple Text Samples**: Random selection from various text passages
- **Responsive Design**: Clean, modern UI built with Tailwind CSS
- **Game Results**: Final score display after completion

## 🛠️ Tech Stack

### Frontend
- **Vue.js 3** - Progressive JavaScript framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Fast build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **Composition API** - Modern Vue.js development approach

### Backend (Planned)
- **Spring Boot** - Java-based framework
- **Spring Web** - REST API development
- **Spring Data JPA** - Database operations
- **H2/PostgreSQL** - Database options
- **Spring WebSocket** - Real-time features

## 📁 Project Structure

```
simple-typing-game/
├── frontend/                 # Vue.js application
│   ├── src/
│   │   ├── App.vue          # Main component
│   │   ├── main.ts          # Application entry point
│   │   └── style.css        # Global styles
│   ├── package.json
│   └── vite.config.ts
├── backend/                 # Spring Boot application (planned)
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
   git clone <your-repo-url>
   cd simple-typing-game
   ```

2. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

3. **Install dependencies**
   ```bash
   pnpm install
   # or
   npm install
   ```

4. **Start development server**
   ```bash
   pnpm dev
   # or
   npm run dev
   ```

5. **Open your browser**
   ```
   http://localhost:5173
   ```

### Backend Setup (Coming Soon)

```bash
cd backend
./mvnw spring-boot:run
```

## 🎯 How to Play

1. Click **"Start Game"** to begin
2. Type the displayed text in the textarea below
3. Watch your **WPM** and **Accuracy** update in real-time
4. Characters turn:
   - 🟢 **Green** - Correctly typed
   - 🔴 **Red** - Incorrectly typed
5. Complete the text or wait for the 60-second timer
6. View your final results!

## 📊 Game Mechanics

- **WPM Calculation**: `(Total Words Typed / Minutes Elapsed)`
- **Accuracy Calculation**: `(Correct Characters / Total Characters) × 100`
- **Timer**: 60 seconds countdown
- **Text Samples**: Randomly selected from predefined passages

## 🔧 Development

### Available Scripts (Frontend)

```bash
# Development server
pnpm dev

# Build for production
pnpm build

# Preview production build
pnpm preview

# Type checking
pnpm type-check
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
- **Footer**: Developer credits

## 📈 Planned Features

- [ ] User authentication and profiles
- [ ] Score history and statistics
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