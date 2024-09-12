package com.kuuhaku

def dict = new File("dict_full.txt")
if (!dict.exists() || !dict.file) return

def words = dict.readLines().collect { Uwuifier.uwu(it) }
words.sort()

int digits = (words.size() as String).length()
def indexes = [:]

def curr = ""
words.eachWithIndex { it, i ->
	def ch = it[0]
	if (ch != curr) {
		indexes[curr = it[0]] = i + 2
		println "${curr.toUpperCase()} -> $i"
	}
}

words.add(0, digits as String)
words.add(1, indexes.collect { k, v -> k + (v as String).padLeft(digits, "0") }.join(""))

def out = new File("uwu_en.dict")
if (!dict.exists() || !dict.file) out.createNewFile()

out.text = words.join("\n")