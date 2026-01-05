import java.io.File

data class Note(
    val id: Int,
    var text: String,
    var isDone: Boolean = false
)

const val FILE_NAME = "my_notes.txt"

fun printMenu() {
    println("--- Menu ---")
    println("1. Add note")
    println("2. List notes")
    println("3. Toggle note done")
    println("4. Delete note")
    println("0. Exit")
    print("Enter your choice > ")
    println()
    println("--- Menu ---")
}
fun SaveNotes(notes: List<Note>) {
    val file = File(FILE_NAME)
    val dataToWrite = notes.joinToString("/n") {note ->
        "${note.id}|${note.isDone}|${note.text}"
    }
    file.writeText(dataToWrite)
}
fun addNote (notes: MutableList<Note>) {
    print("Enter note text > ")
    val text = readlnOrNull()?:return
    if (text.isBlank()) {
        println("Note text cannot be empty")
        return
    }
    val newId = if (notes.isEmpty()) 1 else notes.maxOf { it.id } + 1
    notes.add(Note(newId, text))
    println("Note added successfully with ID $newId")
    SaveNotes(notes)
}
fun viewNotes(notes: MutableList<Note>) {
    if (notes.isEmpty()) {
        println("No notes found")
        return
    }
    println("--- Your notes ---")
    for (note in notes) {
        val status = if (note.isDone) "[x]" else "[ ]"
        println("$status ID:${note.id} - ${note.text}")
    }
}
fun toggleDone(notes: MutableList<Note>) {
    viewNotes(notes)
    if (notes.isEmpty()) return
    print("Enter ID of your note to change it status: ")
    val idStr = readlnOrNull()
    val id = idStr?.toIntOrNull()
    val note = notes.find { it.id == id }
    if (note != null) {
        note.isDone = !note.isDone
        println("Note status changed successfully")
        SaveNotes(notes)
    } else {
        println("Note with ID $id not found")
    }
}
fun deleteNote(notes: MutableList<Note>) {
    viewNotes(notes)
    if (notes.isEmpty()) return
    print("Enter ID of your note to delete it: ")
    val id = readlnOrNull()?.toIntOrNull()
    val removed = notes.removeIf { it.id == id }
    if (removed) {
        println("Note successfully deleted")
        SaveNotes(notes)
    } else {
        println("Note with ID $id not found")
    }
}
fun loadNotes(): MutableList<Note> {
    val file = File(FILE_NAME)
    val list = mutableListOf<Note>()
    if (!file.exists()) return list
    try {
        file.forEachLine { line ->
            val parts = line.split("|")
            if (parts.size >= 3) {
                val id = parts[0].toInt()
                val isDone = parts[1].toBoolean()
                val text = parts[2]
                list.add(Note(id, text, isDone))
            }
        }
    } catch (e: Exception) {
        println("Error loading notes: ${e.message}")
    }
    return list
}
fun main() {
    val notes = loadNotes()
    println("--- Notes Manager ---")
    println("Notes uploaded: ${notes.size}")
    while (true) {
        printMenu()
        val input = readlnOrNull()?.trim()
        when (input) {
            "1" -> addNote(notes)
            "2" -> viewNotes(notes)
            "3" -> toggleDone(notes)
            "4" -> deleteNote(notes)
            "0" -> {
                SaveNotes(notes)
                println("Bye! Notes saved successfully :) ")
                break
            }
            else -> println("Invalid option")
        }
        println()
    }
}