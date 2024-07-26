package com.kuuhaku

def baseURL = "https://voc.iilp.cplp.org/index.php?action=simplesearch&base=form"
def letters = "a".."z"

def extract = (ch, i) -> {
	def content =  "$baseURL&query=$ch&sel=start&&start=$i".toURL().text
	int total = 100
	if (content =~ / \(de (\d+)\)<\/i>/) {
		total = (content =~ / \(de (\d+)\)<\/i>/)[0][1] as int
	}

	def words = []
	(content =~ /<a class=let href="\?action=(\w+?)&.+?><b>(.+?)<\/b>.+?>(\w+)<\/span>/).each {
		def act = it[1]
		def word = it[2]
		def type = it[3]

		words << "$word,$act,$type"
	}

	return [words, total]
}

def dir = new File("dicts")
if (!dir.exists() || !dir.directory) dir.mkdir()

letters.each { ch ->
	def dump = new File(dir, "dict_${ch}.txt")
	if (dump.exists()) return
	dump.createNewFile()

	def (words, total) = extract(ch, 0)
	words.each {
		dump.append "$it\n"
	}

	println "Progress (${ch.toUpperCase()}) - 100 of $total (${100 * 100 / total as int}%)"
	(Math.ceil(total / 100) - 1).times {
		int page = 100 + 100 * it
		extract(ch, page)[0].each {
			dump.append "$it\n"
		}

		println "Progress (${ch.toUpperCase()}) - ${100 + page} of $total (${(100 + page) * 100 / total as int}%)"
		sleep 100
	}
}