# AHSEC Class 12 Physics & Chemistry (Part I) Prep Platform

A comprehensive, production-ready Android examination preparation platform designed specifically for Assam Higher Secondary Education Council (AHSEC) Class 12 Science students. The app covers every chapter in Physics Part I and Chemistry Part I with verified previous years' questions (PYQs) and high-yield syllabus MCQs, featuring interactive Practice Mode, timed Exam Mode with full board analytics, Gemini AI explanations, and a role-gated Administrator Console.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural decisions have been confirmed through user clarification:
> - **Platform Architecture**: Native Android application in Jetpack Compose (Material 3) with an integrated administrative console console providing full desktop-like management controls.
> - **Question Bank Strategy**: High-fidelity verified question bank covering all Part I syllabus chapters (with genuine AHSEC PYQs tagged with exam years) alongside on-demand Gemini AI question expansion and doubt assistance.
> - **Administrative Access**: Role-gated Teacher & Administrator Portal secured by administrative credentials and verification controls.

- **Confirmed Decision 1**: Native Android Jetpack Compose app featuring modern Material 3 styling, offline resilience, and edge-to-edge responsiveness.
- **Confirmed Decision 2**: Comprehensive chapter coverage across 7 Physics chapters and 5 Chemistry chapters (Part I) with clear distinction between authentic verified PYQs and syllabus-based questions.
- **Confirmed Decision 3**: Dual-mode engine: Practice Mode (instant feedback, formula breakdowns, option-by-option analysis) and Exam Mode (randomized questions, countdown timer, question palette, review marking, and post-exam chapter breakdown).
- **Confirmed Decision 4**: Integrated Gemini AI (via `gemini-3.8-flash`) for on-demand step-by-step doubt resolution, conceptual derivations, and admin-supervised question generation.

---

## 1. Overview & Core Concept

### What It Does
The application equips AHSEC Class 12 students with an authentic, rigorous testing environment for Assam Board examinations in Physics (Part I) and Chemistry (Part I). It pairs verified board questions with step-by-step conceptual explanations, tracks progress per chapter, provides simulated board exam conditions, and offers an administrator interface for managing questions and monitoring performance.

### Target Audience / Persona
- **AHSEC Class 12 Science Students**: Preparing for Assam Board examinations, half-yearly exams, and competitive entrances (NEET/JEE fundamentals) requiring focused practice on Part I NCERT/AHSEC syllabus.
- **Teachers & Administrators**: Educators managing curricula, importing curated question banks via CSV/JSON, reviewing student metrics, and maintaining academic accuracy.

### Key Value
- **Board-Specific Accuracy**: Focuses strictly on the AHSEC curriculum without extraneous content, labeling genuine previous year questions (e.g., AHSEC 2024, 2023, 2022, 2020, 2019) with genuine examination records.
- **Adaptive Learning with Gemini AI**: Explains not just *why* an option is correct, but precisely *why* alternative options are incorrect, complete with formula derivations and SI units.
- **Complete Offline Capability**: Core questions, practice progress, and exam history persist locally, ensuring students in areas with intermittent connectivity can study without interruption.

---

## 2. User Experience & Visual Design

```
┌────────────────────────────────────────────────────────────────────────┐
│                        AHSEC Prep 12 (Part I)                          │
├────────────────────────────────────────────────────────────────────────┤
│ [ Student Dashboard ]                                                  │
│  ├─ Subject Switcher: [ Physics Part I ] [ Chemistry Part I ]          │
│  ├─ Quick Stats: Overall Accuracy (78%), Tests Taken (14), MCQs Solved │
│  ├─ Continue Practice Banner: "Current Electricity - 68/120 Solved"    │
│  └─ Chapter Cards (Progress bar, PYQ badge count, Mastery level)       │
├────────────────────────────────────────────────────────────────────────┤
│ [ Practice Mode Screen ]                                               │
│  ├─ Chapter Header & Filter (All / PYQ Only / Numerical)              │
│  ├─ Question Card with Year Tag ("AHSEC 2023" / "Syllabus High-Yield") │
│  ├─ Interactive 4 Options (Green/Red feedback on tap)                 │
│  ├─ Explanation Box: Why A is correct & why B, C, D are incorrect     │
│  └─ "Ask Gemini AI Tutor" (Formula derivation / Concept deep-dive)     │
├────────────────────────────────────────────────────────────────────────┤
│ [ Exam Mode Screen ]                                                   │
│  ├─ Top App Bar: Live Countdown Timer (e.g. 29:45), Question 14/30     │
│  ├─ Question Palette (Answered, Marked for Review, Unattempted)        │
│  ├─ Bottom Controls: [Previous] [Mark for Review] [Next] [Submit]      │
│  └─ Exam Result Sheet: Score Card, Accuracy %, Chapter Breakdown       │
├────────────────────────────────────────────────────────────────────────┤
│ [ Admin Console (Role-Gated) ]                                         │
│  ├─ Overview Metrics: Total Questions, Chapter Counts, Student Stats   │
│  ├─ Question Manager: Add / Edit / Delete / Toggle Publish             │
│  ├─ Bulk Import: JSON & CSV Upload parser                              │
│  └─ AI Generator Studio: Generate & review syllabus questions          │
└────────────────────────────────────────────────────────────────────────┘
```

### Visual Identity & Theme
- **Aesthetic Direction**: Scholarly, disciplined, and crisp. Uses deep Assam tea-indigo and academic cobalt accents with warm slate cards and high-contrast typography.
- **Color Palette**:
  - `Primary`: Deep Royal Navy (`#1A365D`) & Assam Indigo (`#2B6CB0`)
  - `Secondary`: Vibrant Amber Gold (`#D69E2E`) for PYQ badges and stars
  - `Success`: Emerald Green (`#2F855A`) for correct answers
  - `Error`: Crimson Red (`#C53030`) for incorrect answers
  - `Background & Surface`: Clean Crisp White/Soft Cream (`#F7FAFC`) in light mode; Slate Midnight (`#0F172A`) in dark mode.
- **Typography**: Clear, highly legible sans-serif hierarchy tailored for scientific formulas, subscripts, superscripts, and Greek letters ($\Omega$, $\mu$, $\epsilon_0$, $\lambda$).
- **Touch & Accessibility**: Minimum 48dp touch targets on all multiple-choice options, high-contrast states, and accessible TalkBack descriptions.

---

## 3. Key Product Decisions & Trade-Offs

### 1. Chapter Scope (AHSEC Class 12 Part I Syllabus)
- **Physics Part I (7 Chapters)**:
  1. *Electric Charges and Fields*
  2. *Electrostatic Potential and Capacitance*
  3. *Current Electricity*
  4. *Moving Charges and Magnetism*
  5. *Magnetism and Matter*
  6. *Electromagnetic Induction*
  7. *Alternating Current*
- **Chemistry Part I (5 Chapters)**:
  1. *Solutions*
  2. *Electrochemistry*
  3. *Chemical Kinetics*
  4. *The d- and f-Block Elements*
  5. *Coordination Compounds*

### 2. Dual Data Layer (Robust Local Persistence + AI Expansion)
- **Chosen Approach**: Use Android Room Database as the high-speed local engine seeded with verified AHSEC questions, chapter structures, student records, and exam logs. Combined with Gemini AI Logic for on-demand explanation expansion and generative expansion.
- **Why**: Ensures zero latency during exams, works 100% offline without failing during network drops, and allows instant query indexing for search and filter.
- **No Fabricated Records**: Genuine PYQs carry their authentic year (e.g., `AHSEC 2024`, `AHSEC 2022`, `AHSEC 2020`); newly curated questions are explicitly tagged as `Syllabus High-Yield`.

### 3. Student Registration & Security Model
- **Student Profile**: Simple registration matching user specification: Full Name, Address, Phone Number, Email Address, and Password.
- **Admin Portal**: Role-gated access with dedicated administrator authentication. Admins can view analytics, manage questions, import CSV/JSON batches, and trigger AI question generation with manual approval gates.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Android Application Layer                       │
│                                                                        │
│   ┌─────────────────────┐   ┌───────────────────┐   ┌──────────────┐   │
│   │  Student Dashboard  │   │ Practice Engine   │   │ Exam Engine  │   │
│   └──────────┬──────────┘   └─────────┬─────────┘   └───────┬──────┘   │
│              │                        │                     │          │
│   ┌──────────┴────────────────────────┴─────────────────────┴──────┐   │
│   │                     AppViewModel & Navigation                  │   │
│   └──────────┬────────────────────────┬─────────────────────┬──────┘   │
│              │                        │                     │          │
│   ┌──────────┴──────────┐   ┌─────────┴─────────┐   ┌───────┴──────┐   │
│   │ Question Repository │   │  Exam Repository  │   │ User & Auth  │   │
│   └──────────┬──────────┘   └─────────┬─────────┘   └───────┬──────┘   │
│              │                        │                     │          │
│   ┌──────────┴────────────────────────┴─────────────────────┴──────┐   │
│   │                 Room Database (SQLite Local)                   │   │
│   │    - QuestionsEntity (120/chapter, PYQ year, explanations)     │   │
│   │    - ExamAttemptEntity & QuestionResultEntity                  │   │
│   │    - StudentEntity & AdminCredentialEntity                     │   │
│   └────────────────────────────────────────────────────────────────┘   │
│                                       ▲                                │
│                                       │ On-Demand Expansion            │
│   ┌───────────────────────────────────┴────────────────────────────┐   │
│   │             Firebase AI Logic (Gemini 3.8 Flash)               │   │
│   │   - Step-by-step doubt explanation and formula analysis        │   │
│   │   - Structured MCQ generator with strict JSON schema           │   │
│   └────────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────┘
```

### Core Data Models
1. **`QuestionItem`**:
   - `id`: String / Long
   - `subject`: `PHYSICS` or `CHEMISTRY`
   - `chapterId`: Int (1 to 7 for Physics, 1 to 5 for Chemistry)
   - `chapterTitle`: String
   - `questionText`: String (supports formulas and units)
   - `optionA`: String
   - `optionB`: String
   - `optionC`: String
   - `optionD`: String
   - `correctOption`: Char (`'A'`, `'B'`, `'C'`, `'D'`)
   - `explanation`: String
   - `whyIncorrect`: String (explaining why remaining options fail)
   - `isPyq`: Boolean
   - `pyqYear`: String? (e.g. "AHSEC 2024", "AHSEC 2023", "AHSEC 2020")
   - `difficulty`: `EASY` | `MEDIUM` | `HARD`
   - `isPublished`: Boolean
2. **`ExamSession`**:
   - `sessionId`: String
   - `subject`: String
   - `chapterId`: Int? (or 0 for Part I Mock)
   - `totalQuestions`: Int
   - `timeAllowedSeconds`: Int
   - `timeTakenSeconds`: Int
   - `answers`: Map<Int, Char>
   - `markedForReview`: Set<Int>
   - `score`: Int
   - `percentage`: Float
   - `completedAt`: Long
3. **`StudentUser`**:
   - `id`: String
   - `fullName`: String
   - `address`: String
   - `phone`: String
   - `email`: String
   - `passwordHash`: String
   - `role`: `STUDENT` | `ADMIN`

---

## 5. Implementation Phases & Verification Steps

1. **Step 1: Application Identity & Theme Setup**
   - Update `metadata.json` and `res/values/strings.xml` with "AHSEC Class 12 Prep (Part I)".
   - Establish Material 3 educational color palette in `ui/theme/Color.kt` and `Theme.kt`.

2. **Step 2: Database & Domain Layer**
   - Implement Room Database (`AppDatabase`, `QuestionDao`, `ExamDao`, `UserDao`).
   - Seed authentic AHSEC questions covering Physics (Chapters 1-7) and Chemistry (Chapters 1-5).
   - Create Repository layer with reactive Flows.

3. **Step 3: Student Authentication & Dashboard**
   - Implement Registration (Name, Address, Phone, Email, Password) and Login.
   - Build Student Dashboard with chapter progress bars, PYQ badge counts, accuracy analytics, and continue-practice quick launch.

4. **Step 4: Practice Mode Engine**
   - Interactive question cards with immediate green/red feedback.
   - Comprehensive explanation cards breakdown (Why correct + Why other options are wrong).
   - Gemini AI Tutor integration for on-demand conceptual clarifications and formula derivations.

5. **Step 5: Exam Mode Engine**
   - Board exam simulation with randomized questions, customizable countdown timer, and question status palette (Answered / Marked for Review / Unattempted).
   - Auto-submission on timer expiry.
   - Result screen with score, accuracy %, chapter breakdown, and complete answer review.

6. **Step 6: Role-Gated Admin Console**
   - Secure Admin login.
   - Question manager (add/edit/delete/publish).
   - Bulk CSV / JSON import dialog.
   - AI Question Generator studio for expanding the question repository with teacher review.
   - Student performance analytics.

7. **Step 7: Verification & Build**
   - Run `compile_applet` to verify compilation.
   - Test practice mode, exam mode, question filtering, and admin workflows.
