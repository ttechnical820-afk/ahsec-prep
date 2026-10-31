package com.example.data.model

enum class Subject(val displayName: String, val code: String) {
    PHYSICS("Physics (Part I)", "PHYSICS"),
    CHEMISTRY("Chemistry (Part I)", "CHEMISTRY")
}

data class ChapterInfo(
    val id: Int,
    val subject: Subject,
    val number: Int,
    val title: String,
    val keyTopics: String,
    val totalTarget: Int = 120
)

object SyllabusCatalog {
    val physicsChapters = listOf(
        ChapterInfo(
            id = 1,
            subject = Subject.PHYSICS,
            number = 1,
            title = "Electric Charges and Fields",
            keyTopics = "Coulomb's law, Gauss's law, Electric flux, Dipole in uniform field"
        ),
        ChapterInfo(
            id = 2,
            subject = Subject.PHYSICS,
            number = 2,
            title = "Electrostatic Potential & Capacitance",
            keyTopics = "Equipotential surfaces, Potential energy, Parallel plate capacitor, Dielectric effect"
        ),
        ChapterInfo(
            id = 3,
            subject = Subject.PHYSICS,
            number = 3,
            title = "Current Electricity",
            keyTopics = "Ohm's law, Drift velocity, Kirchhoff's laws, Wheatstone bridge, Temperature coefficient"
        ),
        ChapterInfo(
            id = 4,
            subject = Subject.PHYSICS,
            number = 4,
            title = "Moving Charges and Magnetism",
            keyTopics = "Biot-Savart law, Ampere's circuital law, Lorentz force, Solenoid, Galvanometer"
        ),
        ChapterInfo(
            id = 5,
            subject = Subject.PHYSICS,
            number = 5,
            title = "Magnetism and Matter",
            keyTopics = "Bar magnet, Magnetic dipole, Earth's magnetism, Dia, Para & Ferromagnetism"
        ),
        ChapterInfo(
            id = 6,
            subject = Subject.PHYSICS,
            number = 6,
            title = "Electromagnetic Induction",
            keyTopics = "Faraday's laws, Lenz's law, Motional EMF, Self & Mutual inductance, Eddy currents"
        ),
        ChapterInfo(
            id = 7,
            subject = Subject.PHYSICS,
            number = 7,
            title = "Alternating Current",
            keyTopics = "Peak & RMS values, LCR series circuit, Resonance, Power factor, Transformer"
        )
    )

    val chemistryChapters = listOf(
        ChapterInfo(
            id = 1,
            subject = Subject.CHEMISTRY,
            number = 1,
            title = "Solutions",
            keyTopics = "Raoult's law, Colligative properties, van 't Hoff factor, Henry's law, Molarity"
        ),
        ChapterInfo(
            id = 2,
            subject = Subject.CHEMISTRY,
            number = 2,
            title = "Electrochemistry",
            keyTopics = "Nernst equation, Conductance, Kohlrausch's law, Galvanic cells, Fuel cells"
        ),
        ChapterInfo(
            id = 3,
            subject = Subject.CHEMISTRY,
            number = 3,
            title = "Chemical Kinetics",
            keyTopics = "Rate of reaction, Order & molecularity, Integrated rate laws, Arrhenius equation"
        ),
        ChapterInfo(
            id = 4,
            subject = Subject.CHEMISTRY,
            number = 4,
            title = "The d- and f-Block Elements",
            keyTopics = "Transition elements properties, Oxidation states, Lanthanoid contraction, KMnO4 & K2Cr2O7"
        ),
        ChapterInfo(
            id = 5,
            subject = Subject.CHEMISTRY,
            number = 5,
            title = "Coordination Compounds",
            keyTopics = "Werner's theory, IUPAC nomenclature, Isomerism, Valence bond theory, Crystal field theory"
        )
    )

    fun getChaptersForSubject(subject: Subject): List<ChapterInfo> {
        return if (subject == Subject.PHYSICS) physicsChapters else chemistryChapters
    }

    fun getChapterTitle(subject: Subject, chapterId: Int): String {
        val list = getChaptersForSubject(subject)
        return list.find { it.id == chapterId }?.title ?: "Chapter $chapterId"
    }
}
