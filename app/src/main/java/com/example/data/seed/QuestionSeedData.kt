package com.example.data.seed

import com.example.data.local.QuestionEntity

object QuestionSeedData {

    fun getInitialQuestions(): List<QuestionEntity> {
        val list = mutableListOf<QuestionEntity>()

        // ==========================================
        // PHYSICS PART I - CHAPTER 1: Electric Charges and Fields
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 1,
                chapterTitle = "Electric Charges and Fields",
                questionText = "The SI unit of electric permittivity of free space (ε₀) is:",
                optionA = "C² N⁻¹ m⁻²",
                optionB = "N m² C⁻²",
                optionC = "N C⁻¹ m⁻¹",
                optionD = "C N m⁻²",
                correctOption = "A",
                explanation = "From Coulomb's Law, F = (1 / 4πε₀) * (q₁q₂ / r²). Rearranging gives ε₀ = (q₁q₂) / (4π F r²). Substituting SI units gives C * C / (N * m²) = C² N⁻¹ m⁻².",
                whyIncorrect = "Option B (N m² C⁻²) is the unit of 1/(4πε₀), the electrostatic constant k. Option C and D have mismatched dimensional units.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 1,
                chapterTitle = "Electric Charges and Fields",
                questionText = "An electric dipole of dipole moment p is placed in a uniform electric field E. The torque τ acting on it is given by:",
                optionA = "τ = p · E",
                optionB = "τ = p × E",
                optionC = "τ = E × p",
                optionD = "τ = p + E",
                correctOption = "B",
                explanation = "The torque on a dipole in a uniform electric field is given by τ = p × E, having magnitude |τ| = p E sin θ, directed perpendicular to the plane containing p and E.",
                whyIncorrect = "Option A (p · E) represents potential energy U = -p · E, a scalar. Option C is the negative of the actual torque vector. Option D is dimensionally invalid.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 1,
                chapterTitle = "Electric Charges and Fields",
                questionText = "Total electric flux emerging through a closed surface enclosing a charge q in vacuum is:",
                optionA = "q / ε₀",
                optionB = "ε₀ / q",
                optionC = "q · ε₀",
                optionD = "4π ε₀ q",
                correctOption = "A",
                explanation = "According to Gauss's Theorem, the total electric flux Φ through any closed surface is equal to 1/ε₀ times the total net charge enclosed by the surface: Φ = ∮ E · dA = q_enclosed / ε₀.",
                whyIncorrect = "Option B is the reciprocal. Option C multiplies instead of divides by ε₀. Option D introduces an unnecessary 4π factor.",
                isPyq = true,
                pyqYear = "AHSEC 2020",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 1,
                chapterTitle = "Electric Charges and Fields",
                questionText = "When a glass rod is rubbed with silk cloth, the glass rod becomes:",
                optionA = "Negatively charged by gaining electrons",
                optionB = "Positively charged by losing electrons",
                optionC = "Positively charged by gaining protons",
                optionD = "Neutral with no net charge",
                correctOption = "B",
                explanation = "Electrons are more loosely bound in glass than in silk. Upon friction, electrons transfer from the glass rod to silk, leaving the glass with a net positive charge due to deficiency of electrons.",
                whyIncorrect = "Option A is what happens to the silk cloth. Option C is physically impossible under friction because protons are tightly bound in atomic nuclei. Option D is incorrect as charges are transferred.",
                isPyq = true,
                pyqYear = "AHSEC 2019",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 1,
                chapterTitle = "Electric Charges and Fields",
                questionText = "Two point charges +4q and +q are placed at a distance L apart. Where should a third charge Q be placed on the line connecting them so that the net force on Q is zero?",
                optionA = "L/3 from +q",
                optionB = "L/3 from +4q",
                optionC = "L/2 from either charge",
                optionD = "2L/3 from +q",
                correctOption = "A",
                explanation = "Let distance from +4q be x, so distance from +q is (L - x). Equating forces: k(4q)Q/x² = k(q)Q/(L - x)². Taking square root: 2/x = 1/(L - x) => 2L - 2x = x => 3x = 2L => x = 2L/3 from +4q, which corresponds to L - 2L/3 = L/3 from +q.",
                whyIncorrect = "Option B would yield 4 times the force from +4q. Option C is true only for equal charges. Option D gives distance from +4q, not from +q.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 2: Electrostatic Potential & Capacitance
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 2,
                chapterTitle = "Electrostatic Potential & Capacitance",
                questionText = "The work done in moving a test charge q over an equipotential surface between two points separated by distance d is:",
                optionA = "q · d",
                optionB = "Zero",
                optionC = "q / (4πε₀ d)",
                optionD = "Infinite",
                correctOption = "B",
                explanation = "By definition, the electric potential V is identical at all points on an equipotential surface. Hence potential difference ΔV = 0. Since work done W = q · ΔV, W = 0.",
                whyIncorrect = "Options A, C and D suggest non-zero work, violating the definition of an equipotential surface where electric field lines are always perpendicular to the surface.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 2,
                chapterTitle = "Electrostatic Potential & Capacitance",
                questionText = "If a dielectric slab of dielectric constant K is inserted between the plates of an isolated charged parallel plate capacitor, its capacitance:",
                optionA = "Decreases by a factor of K",
                optionB = "Increases by a factor of K",
                optionC = "Remains unchanged",
                optionD = "Becomes zero",
                correctOption = "B",
                explanation = "The new capacitance C' with a dielectric slab filling the entire space is C' = K * C₀, where C₀ = ε₀A/d. Because K > 1 for all dielectric materials, capacitance increases K-fold.",
                whyIncorrect = "Option A is inverted. Option C would occur only if vacuum was retained. Option D is physically invalid.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 2,
                chapterTitle = "Electrostatic Potential & Capacitance",
                questionText = "The electric potential at an equatorial point of an electric dipole of dipole moment p is:",
                optionA = "p / (4πε₀ r²)",
                optionB = "Zero",
                optionC = "2p / (4πε₀ r³)",
                optionD = "-p / (4πε₀ r)",
                correctOption = "B",
                explanation = "At any point on the equatorial plane of a dipole, the distance to the positive charge is equal to the distance to the negative charge (r₊ = r₋). The potentials cancel: V = kq/r₊ + k(-q)/r₋ = 0.",
                whyIncorrect = "Option A is the potential on the axial line. Option C represents the electric field on the axial axis. Option D is incorrect.",
                isPyq = true,
                pyqYear = "AHSEC 2020",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 2,
                chapterTitle = "Electrostatic Potential & Capacitance",
                questionText = "Three capacitors of capacitance 6 μF each are connected in series. Their equivalent capacitance is:",
                optionA = "18 μF",
                optionB = "2 μF",
                optionC = "3 μF",
                optionD = "1 μF",
                correctOption = "B",
                explanation = "For capacitors in series, 1/C_eq = 1/C₁ + 1/C₂ + 1/C₃ = 1/6 + 1/6 + 1/6 = 3/6 = 1/2 μF⁻¹. Thus C_eq = 2 μF.",
                whyIncorrect = "Option A is the parallel combination (6 + 6 + 6 = 18 μF). Option C and D represent arithmetic errors.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 3: Current Electricity
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 3,
                chapterTitle = "Current Electricity",
                questionText = "Kirchhoff's first law (Junction rule) at a node in an electric circuit is based on the conservation of:",
                optionA = "Energy",
                optionB = "Electric Charge",
                optionC = "Linear Momentum",
                optionD = "Angular Momentum",
                correctOption = "B",
                explanation = "Kirchhoff's junction rule states Σ I = 0, meaning total current entering a junction equals total current leaving it. Since current is charge per unit time and charge cannot accumulate at a junction, it directly follows charge conservation.",
                whyIncorrect = "Option A (Energy) is the basis of Kirchhoff's Second Law (Loop rule). Options C and D are conservation principles in mechanics, not circuit nodes.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 3,
                chapterTitle = "Current Electricity",
                questionText = "If a wire of resistance R is stretched uniformly to double its original length, its new resistance will be:",
                optionA = "2 R",
                optionB = "4 R",
                optionC = "R / 2",
                optionD = "R / 4",
                correctOption = "B",
                explanation = "Volume remains constant during stretching: V = A * L = A' * (2L), so A' = A/2. New resistance R' = ρ * (2L) / (A/2) = 4 * (ρ L / A) = 4R.",
                whyIncorrect = "Option A ignores the reduction in cross-sectional area during uniform stretching. Options C and D reflect shortening or compressing rather than stretching.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "MEDIUM"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 3,
                chapterTitle = "Current Electricity",
                questionText = "Drift velocity v_d of electrons in a conductor of electron density n, cross-section A, carrying current I is:",
                optionA = "I / (n e A)",
                optionB = "n e A / I",
                optionC = "I * n * e / A",
                optionD = "e A / (n I)",
                correctOption = "A",
                explanation = "The fundamental relationship between current and drift speed is I = n e A v_d. Solving for drift velocity yields v_d = I / (n e A).",
                whyIncorrect = "Option B is the inverted ratio. Option C and D have incorrect dimensional arrangements.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 3,
                chapterTitle = "Current Electricity",
                questionText = "The temperature coefficient of resistance is negative for:",
                optionA = "Copper",
                optionB = "Silver",
                optionC = "Silicon and Semiconductors",
                optionD = "Nichrome",
                correctOption = "C",
                explanation = "In semiconductors like Silicon and Germanium, increasing temperature breaks covalent bonds, sharply increasing charge carrier concentration n. The exponential rise in n dominates over the slight decrease in relaxation time τ, decreasing resistivity (negative temperature coefficient).",
                whyIncorrect = "Options A, B, and D are metallic conductors or alloys where resistance increases with temperature (positive temperature coefficient).",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 4: Moving Charges and Magnetism
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 4,
                chapterTitle = "Moving Charges and Magnetism",
                questionText = "The magnetic field B at the center of a circular coil of radius R carrying current I is:",
                optionA = "μ₀ I / (2 R)",
                optionB = "μ₀ I / (4π R)",
                optionC = "μ₀ I / (2π R)",
                optionD = "μ₀ I R / 2",
                correctOption = "A",
                explanation = "According to Biot-Savart Law, integrating over the circular perimeter at the center yields B = (μ₀ / 4π) * (I * 2πR / R²) = μ₀ I / (2R).",
                whyIncorrect = "Option B is missing a factor of 2π. Option C is the field due to an infinitely long straight wire, not a circular loop center. Option D has incorrect dimensions.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 4,
                chapterTitle = "Moving Charges and Magnetism",
                questionText = "A charged particle moves with velocity v parallel to a uniform magnetic field B. The magnetic force experienced by it is:",
                optionA = "q v B",
                optionB = "Zero",
                optionC = "q v B / 2",
                optionD = "Infinite",
                correctOption = "B",
                explanation = "The magnetic Lorentz force is given by F = q (v × B) = q v B sin θ. When moving parallel to the magnetic field, angle θ = 0°, so sin 0° = 0 and F = 0.",
                whyIncorrect = "Option A corresponds to perpendicular motion (θ = 90°). Option C corresponds to θ = 30°. Option D is physically impossible.",
                isPyq = true,
                pyqYear = "AHSEC 2021",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 4,
                chapterTitle = "Moving Charges and Magnetism",
                questionText = "To convert a moving coil galvanometer into an ammeter, one should connect a:",
                optionA = "High resistance in series",
                optionB = "Low resistance (shunt) in parallel",
                optionC = "High resistance in parallel",
                optionD = "Low resistance in series",
                correctOption = "B",
                explanation = "An ammeter must have very low total resistance to avoid altering circuit current. Connecting a small shunt resistance S in parallel diverts the bulk of current around the sensitive galvanometer coil.",
                whyIncorrect = "Option A (high resistance in series) converts a galvanometer into a voltmeter. Options C and D are improper configurations.",
                isPyq = true,
                pyqYear = "AHSEC 2020",
                difficulty = "EASY"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 5: Magnetism and Matter
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 5,
                chapterTitle = "Magnetism and Matter",
                questionText = "The magnetic susceptibility (χ) of a diamagnetic substance is:",
                optionA = "Positive and large",
                optionB = "Negative and small",
                optionC = "Positive and small",
                optionD = "Zero",
                correctOption = "B",
                explanation = "Diamagnetic materials develop weak magnetization opposite to the external applied field. Hence susceptibility χ is negative and typically very small (order of 10⁻⁵ to 10⁻⁹).",
                whyIncorrect = "Option A describes ferromagnetic substances. Option C describes paramagnetic substances. Option D describes vacuum.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 5,
                chapterTitle = "Magnetism and Matter",
                questionText = "At the magnetic poles of the Earth, the angle of dip (magnetic inclination) is:",
                optionA = "0°",
                optionB = "45°",
                optionC = "90°",
                optionD = "180°",
                correctOption = "C",
                explanation = "At the Earth's magnetic poles, the magnetic field lines are completely vertical, pointing directly into or out of the Earth's surface. Hence the angle made with the horizontal (angle of dip) is 90°.",
                whyIncorrect = "Option A (0°) is the angle of dip at the magnetic equator where the field is strictly horizontal. 45° and 180° are incorrect.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 6: Electromagnetic Induction
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 6,
                chapterTitle = "Electromagnetic Induction",
                questionText = "Lenz's Law of electromagnetic induction is a direct consequence of the law of conservation of:",
                optionA = "Charge",
                optionB = "Energy",
                optionC = "Momentum",
                optionD = "Mass",
                correctOption = "B",
                explanation = "Lenz's Law states that the polarity of induced EMF always opposes the change in magnetic flux producing it. Mechanical work done in overcoming this opposition is transformed into electrical energy, satisfying energy conservation.",
                whyIncorrect = "Option A applies to Kirchhoff's current law. Options C and D are unrelated to the opposing magnetic force mechanism.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 6,
                chapterTitle = "Electromagnetic Induction",
                questionText = "The SI unit of self-inductance and mutual inductance is:",
                optionA = "Weber",
                optionB = "Henry (H)",
                optionC = "Tesla",
                optionD = "Farad",
                correctOption = "B",
                explanation = "Inductance L is defined as EMF / (rate of change of current) = V / (A/s) = V·s / A, termed Henry (H).",
                whyIncorrect = "Option A (Weber) is magnetic flux. Option C (Tesla) is magnetic flux density. Option D (Farad) is capacitance.",
                isPyq = true,
                pyqYear = "AHSEC 2019",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 6,
                chapterTitle = "Electromagnetic Induction",
                questionText = "Eddy currents are produced in a metallic conductor when it is:",
                optionA = "Placed in a steady magnetic field",
                optionB = "Placed in a changing magnetic field",
                optionC = "Heated to high temperature",
                optionD = "Carrying direct current",
                correctOption = "B",
                explanation = "When bulk pieces of conductors are subjected to changing magnetic flux, induced circulating currents called eddy currents (or Foucault currents) are produced throughout the volume.",
                whyIncorrect = "Option A has zero flux change (dΦ/dt = 0), so no induced EMF. Option C thermal effects don't generate coherent macroscopic eddy loops. Option D is uniform DC.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // PHYSICS PART I - CHAPTER 7: Alternating Current
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 7,
                chapterTitle = "Alternating Current",
                questionText = "The root-mean-square (RMS) value of an AC current I = I₀ sin(ωt) is related to its peak value I₀ by:",
                optionA = "I_rms = I₀ / √2 ≈ 0.707 I₀",
                optionB = "I_rms = 2 I₀ / π",
                optionC = "I_rms = √2 I₀",
                optionD = "I_rms = I₀ / 2",
                correctOption = "A",
                explanation = "Integrating I² over a full cycle: I_rms = √( (1/T) ∫₀ᵀ I₀² sin²(ωt) dt ) = I₀ / √2 ≈ 0.707 I₀.",
                whyIncorrect = "Option B is the mean/average value over half a cycle (2I₀/π ≈ 0.637 I₀). Option C is larger than peak value. Option D is exactly half.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 7,
                chapterTitle = "Alternating Current",
                questionText = "In a series LCR resonant circuit, the impedance Z of the circuit is:",
                optionA = "Maximum and purely inductive",
                optionB = "Minimum and equal to resistance R",
                optionC = "Zero",
                optionD = "Equal to ωL - 1/(ωC)",
                correctOption = "B",
                explanation = "At resonance, inductive reactance X_L equals capacitive reactance X_C (ωL = 1/ωC). Hence impedance Z = √(R² + (X_L - X_C)²) reduces to Z = R, which is minimum, resulting in maximum current.",
                whyIncorrect = "Option A is the opposite; impedance is minimum, not maximum. Option C ignores circuit resistance R. Option D is net reactance.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "MEDIUM"
            )
        )
        list.add(
            QuestionEntity(
                subject = "PHYSICS",
                chapterId = 7,
                chapterTitle = "Alternating Current",
                questionText = "The power factor in an AC circuit with purely inductive or purely capacitive load is:",
                optionA = "1",
                optionB = "0",
                optionC = "0.5",
                optionD = "-1",
                correctOption = "B",
                explanation = "For a pure inductor or pure capacitor, the phase difference between voltage and current is Φ = π/2 (90°). The power factor is cos(Φ) = cos(90°) = 0. No true power is consumed (wattless current).",
                whyIncorrect = "Option A (1) is the power factor for a purely resistive circuit or at resonance. Options C and D are incorrect.",
                isPyq = true,
                pyqYear = "AHSEC 2020",
                difficulty = "EASY"
            )
        )

        // ==========================================
        // CHEMISTRY PART I - CHAPTER 1: Solutions
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 1,
                chapterTitle = "Solutions",
                questionText = "Which of the following concentration terms is independent of temperature?",
                optionA = "Molarity",
                optionB = "Molality",
                optionC = "Normality",
                optionD = "Formality",
                correctOption = "B",
                explanation = "Molality (m) is defined as moles of solute per kilogram of solvent. Since both mass of solute and mass of solvent do not expand or contract with temperature, molality is independent of temperature.",
                whyIncorrect = "Options A, C, and D are volume-dependent concentration terms (per liter of solution). Because liquid volume changes with temperature, their values change with temperature.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 1,
                chapterTitle = "Solutions",
                questionText = "The value of van 't Hoff factor (i) for complete dissociation of BaCl₂ in aqueous solution is:",
                optionA = "1",
                optionB = "2",
                optionC = "3",
                optionD = "0.5",
                correctOption = "C",
                explanation = "BaCl₂ dissociates as: BaCl₂ (aq) → Ba²⁺ + 2 Cl⁻. One formula unit produces 1 barium cation and 2 chloride anions, giving a total of 3 ions. For complete dissociation (α = 1), i = 1 + (n - 1)α = 1 + (3 - 1)(1) = 3.",
                whyIncorrect = "Option A is for non-electrolytes like glucose or urea. Option B is for binary electrolytes like NaCl. Option D would imply association.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "MEDIUM"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 1,
                chapterTitle = "Solutions",
                questionText = "According to Raoult's Law for a solution containing non-volatile solute, relative lowering of vapour pressure is equal to:",
                optionA = "Mole fraction of the solute (x₂)",
                optionB = "Mole fraction of the solvent (x₁)",
                optionC = "Molality of the solution",
                optionD = "Molarity of the solution",
                correctOption = "A",
                explanation = "Raoult's Law establishes that (P₁° - P₁) / P₁° = x₂, where (P₁° - P₁) / P₁° is the relative lowering of vapour pressure and x₂ is the mole fraction of the non-volatile solute.",
                whyIncorrect = "Option B is mole fraction of solvent. The actual vapour pressure is P₁ = P₁° x₁, but lowering is proportional to solute fraction x₂. Options C and D are dimensions of concentration.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 1,
                chapterTitle = "Solutions",
                questionText = "An azeotropic mixture of two liquids boils at a temperature lower than both liquids when it shows:",
                optionA = "Large negative deviation from Raoult's law",
                optionB = "Large positive deviation from Raoult's law",
                optionC = "No deviation (Ideal solution)",
                optionD = "Zero vapour pressure",
                correctOption = "B",
                explanation = "Solutions showing large positive deviations from Raoult's law have weaker solute-solvent intermolecular attractions, leading to higher vapour pressure than expected and forming a minimum-boiling azeotrope.",
                whyIncorrect = "Option A forms a maximum-boiling azeotrope (boiling at higher temperature). Option C cannot form an azeotrope. Option D is physically impossible.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // CHEMISTRY PART I - CHAPTER 2: Electrochemistry
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 2,
                chapterTitle = "Electrochemistry",
                questionText = "The SI unit of molar conductivity (Λ_m) is:",
                optionA = "S m² mol⁻¹",
                optionB = "S m⁻¹ mol⁻¹",
                optionC = "S cm mol⁻¹",
                optionD = "Ω m² mol⁻¹",
                correctOption = "A",
                explanation = "Molar conductivity is defined as Λ_m = κ / c. In SI units, electrolytic conductivity κ is in S m⁻¹ and concentration c is in mol m⁻³, yielding S m⁻¹ / (mol m⁻³) = S m² mol⁻¹ (or S cm² mol⁻¹ in CGS).",
                whyIncorrect = "Option B is the unit of κ / c without squared length. Option D uses resistance units (Ohms) instead of conductance units (Siemens).",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 2,
                chapterTitle = "Electrochemistry",
                questionText = "According to Kohlrausch's law of independent migration of ions, limiting molar conductivity of an electrolyte is equal to:",
                optionA = "Product of individual ionic conductivities",
                optionB = "Sum of individual contributions of the anion and cation",
                optionC = "Difference between cationic and anionic conductivities",
                optionD = "Reciprocal of total cell constant",
                correctOption = "B",
                explanation = "Kohlrausch's law states that limiting molar conductivity Λ°_m of an electrolyte can be represented as the sum of the individual contributions of the cation (λ°₊) and anion (λ°₋): Λ°_m = ν₊ λ°₊ + ν₋ λ°₋.",
                whyIncorrect = "Options A, C, and D misrepresent the linear additive relationship of independent migration at infinite dilution.",
                isPyq = true,
                pyqYear = "AHSEC 2021",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 2,
                chapterTitle = "Electrochemistry",
                questionText = "In a lead storage battery during discharging, the reaction occurring at the anode is:",
                optionA = "Pb(s) + SO₄²⁻(aq) → PbSO₄(s) + 2e⁻",
                optionB = "PbO₂(s) + 4H⁺ + SO₄²⁻ + 2e⁻ → PbSO₄(s) + 2H₂O",
                optionC = "2PbSO₄(s) + 2H₂O → Pb + PbO₂ + 2H₂SO₄",
                optionD = "H₂ + 2OH⁻ → 2H₂O + 2e⁻",
                correctOption = "A",
                explanation = "During discharging, spongy lead (Pb) acts as anode and undergoes oxidation: Pb(s) + SO₄²⁻(aq) → PbSO₄(s) + 2e⁻.",
                whyIncorrect = "Option B is the cathodic reduction of PbO₂. Option C is the overall recharging reaction. Option D is the anode reaction in an alkaline fuel cell.",
                isPyq = true,
                pyqYear = "AHSEC 2020",
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // CHEMISTRY PART I - CHAPTER 3: Chemical Kinetics
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 3,
                chapterTitle = "Chemical Kinetics",
                questionText = "The half-life period (t₁/₂) of a first-order reaction is:",
                optionA = "Directly proportional to initial concentration",
                optionB = "Inversely proportional to initial concentration",
                optionC = "Independent of initial concentration",
                optionD = "Proportional to square of initial concentration",
                correctOption = "C",
                explanation = "For a first-order reaction, integrated rate equation yields t₁/₂ = ln(2) / k = 0.693 / k. Because the rate constant k is constant at a fixed temperature, t₁/₂ is completely independent of the initial reactant concentration [A]₀.",
                whyIncorrect = "Option A is true for zero-order reactions (t₁/₂ = [A]₀ / 2k). Option B is true for second-order reactions (t₁/₂ = 1 / (k [A]₀)).",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 3,
                chapterTitle = "Chemical Kinetics",
                questionText = "The unit of rate constant for a zero-order reaction is:",
                optionA = "s⁻¹",
                optionB = "mol L⁻¹ s⁻¹",
                optionC = "L mol⁻¹ s⁻¹",
                optionD = "L² mol⁻² s⁻¹",
                correctOption = "B",
                explanation = "Rate = k [A]⁰ = k. Since reaction rate is change in molar concentration per unit time, Rate unit = (mol L⁻¹) / s = mol L⁻¹ s⁻¹. Therefore, k has the unit mol L⁻¹ s⁻¹.",
                whyIncorrect = "Option A (s⁻¹) is the unit for a first-order reaction. Option C (L mol⁻¹ s⁻¹) is for second-order. Option D is for third-order.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 3,
                chapterTitle = "Chemical Kinetics",
                questionText = "According to the Arrhenius equation k = A e^(-E_a / RT), a plot of ln(k) versus 1/T yields a straight line with slope equal to:",
                optionA = "-E_a / R",
                optionB = "E_a / R",
                optionC = "-E_a / (2.303 R)",
                optionD = "ln(A)",
                correctOption = "A",
                explanation = "Taking natural logarithm: ln(k) = ln(A) - (E_a / R) * (1/T). Comparing with the linear equation y = mx + c, with y = ln(k) and x = 1/T, the slope m is -E_a / R.",
                whyIncorrect = "Option B has the wrong sign. Option C is the slope when plotting log₁₀(k) (base 10) instead of natural logarithm ln(k). Option D is the y-intercept.",
                isPyq = true,
                pyqYear = "AHSEC 2019",
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // CHEMISTRY PART I - CHAPTER 4: The d- and f-Block Elements
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 4,
                chapterTitle = "The d- and f-Block Elements",
                questionText = "Which of the following 3d-transition series elements exhibits the highest oxidation state (+7)?",
                optionA = "Chromium (Cr)",
                optionB = "Manganese (Mn)",
                optionC = "Iron (Fe)",
                optionD = "Vanadium (V)",
                correctOption = "B",
                explanation = "Manganese has ground state electronic configuration [Ar] 3d⁵ 4s². By participating all five 3d electrons and both two 4s electrons in bonding (as in KMnO₄ / Mn₂O₇), it displays the maximum oxidation state of +7.",
                whyIncorrect = "Option A (Cr) exhibits a maximum of +6 (as in K₂Cr₂O₇). Option C (Fe) rarely exceeds +3 (ferrate +6 is unstable). Option D (V) exhibits up to +5.",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 4,
                chapterTitle = "The d- and f-Block Elements",
                questionText = "Lanthanoid contraction is caused by:",
                optionA = "Imperfect shielding of 4f electrons",
                optionB = "Strong shielding of 4f electrons",
                optionC = "Decreasing nuclear charge",
                optionD = "Expansion of 5d orbitals",
                correctOption = "A",
                explanation = "The 4f orbitals have diffused, complex spatial shapes. Consequently, 4f electrons provide very poor shielding for outer electrons against increasing nuclear charge, causing atomic and ionic radii to shrink steadily across the series.",
                whyIncorrect = "Option B is incorrect because 4f electrons shield poorly, not strongly. Option C is false because nuclear charge Z increases by 1 at each element.",
                isPyq = true,
                pyqYear = "AHSEC 2023",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 4,
                chapterTitle = "The d- and f-Block Elements",
                questionText = "Transition metals and their compounds act as good catalysts primarily due to their ability to:",
                optionA = "Adopt variable oxidation states and form reaction intermediates",
                optionB = "Emit photons upon activation",
                optionC = "Form ionic crystals with high melting points",
                optionD = "Resist all chemical changes",
                correctOption = "A",
                explanation = "Transition metals have partially filled d-orbitals that allow them to adopt variable oxidation states, provide vacant coordination sites, and form unstable intermediate complexes with lower activation energies.",
                whyIncorrect = "Options B, C, and D are physical characteristics that do not account for the catalytic mechanism of surface adsorption and intermediate formation.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        // ==========================================
        // CHEMISTRY PART I - CHAPTER 5: Coordination Compounds
        // ==========================================
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 5,
                chapterTitle = "Coordination Compounds",
                questionText = "The IUPAC name of the complex [Co(NH₃)₅(Cl)]Cl₂ is:",
                optionA = "Pentaamminechloridocobalt(III) chloride",
                optionB = "Pentaamminechlorocobalt(II) chloride",
                optionC = "Chloropentaamminecobalt(III) dichloride",
                optionD = "Cobalt(III) pentaamminechloride",
                correctOption = "A",
                explanation = "Ligands are named alphabetically: 'ammine' precedes 'chlorido'. NH₃ is neutral, Cl is -1, and two ionizable Cl⁻ outside coordination sphere carry -2 charge. Thus cobalt oxidation state x + 5(0) - 1 - 2 = 0 => x = +3. Hence: Pentaamminechloridocobalt(III) chloride.",
                whyIncorrect = "Option B gives incorrect +2 oxidation state. Option C lists ligands in wrong alphabetical order and says 'dichloride' (prefixes are omitted for counter-ions).",
                isPyq = true,
                pyqYear = "AHSEC 2024",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 5,
                chapterTitle = "Coordination Compounds",
                questionText = "Which of the following ligands is a bidentate chelating ligand?",
                optionA = "Oxalate ion (C₂O₄²⁻)",
                optionB = "Chloride ion (Cl⁻)",
                optionC = "Water (H₂O)",
                optionD = "Ammonia (NH₃)",
                correctOption = "A",
                explanation = "Oxalate (C₂O₄²⁻ or ox) possesses two donor oxygen atoms capable of simultaneously coordinating with a single central metal ion, forming a stable 5-membered chelate ring.",
                whyIncorrect = "Options B, C, and D are monodentate ligands possessing only one donor atom coordinating at a time.",
                isPyq = true,
                pyqYear = "AHSEC 2022",
                difficulty = "EASY"
            )
        )
        list.add(
            QuestionEntity(
                subject = "CHEMISTRY",
                chapterId = 5,
                chapterTitle = "Coordination Compounds",
                questionText = "According to Crystal Field Theory, in an octahedral coordination entity, the d-orbitals split into:",
                optionA = "Lower energy t_2g set (triply degenerate) and higher energy e_g set (doubly degenerate)",
                optionB = "Higher energy t_2g set and lower energy e_g set",
                optionC = "Five orbitals of equal energy",
                optionD = "Three sets of energy levels",
                correctOption = "A",
                explanation = "In an octahedral field, ligands approach along the Cartesian axes (x, y, z). The axial d-orbitals (dx²-y², dz²) experience greater electrostatic repulsion and are raised in energy (e_g set), while the non-axial d-orbitals (dxy, dyz, dzx) are stabilized at lower energy (t_2g set).",
                whyIncorrect = "Option B is the splitting pattern for tetrahedral complexes. Option C is the unsplit degenerate state in spherical field. Option D is incorrect.",
                isPyq = false,
                pyqYear = null,
                difficulty = "MEDIUM"
            )
        )

        return list
    }
}
