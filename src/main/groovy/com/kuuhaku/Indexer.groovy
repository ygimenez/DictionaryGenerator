package com.kuuhaku

def dict = new File("dict_full.txt")
if (!dict.exists() || !dict.file) return

def words = dict.readLines()
words.sort()

int digits = (words.size() as String).length()
def indexes = [:]
def letters = ("a".."z").toList()

def curr = letters.removeFirst()
words.eachWithIndex { it, i ->
	if (curr && it.startsWith(curr)) {
		println "${curr.toUpperCase()} -> $i"

		indexes[curr] = i
		curr = letters ? letters.removeFirst() : ""
	}
}

words.add(0, digits as String)
words.add(1, indexes.collect { k, v -> k + (v as String).padLeft(digits, "0") }.join(""))

def out = new File("pt.dict")
if (!dict.exists() || !dict.file) out.createNewFile()

out.text = words.join("\n")