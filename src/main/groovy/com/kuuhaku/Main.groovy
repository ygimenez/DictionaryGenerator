package com.kuuhaku

import org.apache.commons.text.StringEscapeUtils

class Main {
	static def letters = "a".."z"

	static void main(String[] args) {
		en()
	}

	static void en() {
		def baseURL = "https://www.dictionary.com/list"

		def extract = (ch, i) -> {
			if (i == 0) {
				def match = "$baseURL/$ch/9999".toURL().text =~ /<!-- --> \(Page (\d+)\)/

				int total = 1
				if (match) {
					total = match[0][1] as int
				}

				return [[], total]
			}

			def content =  "$baseURL/$ch/$i".toURL().text
			def words = []
			(content =~ /<a href="\/browse\/.+?>(.+?)<\/a>/).each {
				def word = StringEscapeUtils.unescapeHtml4(it[1] as String)
				words << "$word,lemma"
			}

			return [words, 0]
		}

		def dir = new File("dicts_en")
		if (!dir.exists() || !dir.directory) dir.mkdir()

		letters.each { ch ->
			def dump = new File(dir, "dict_${ch}.txt")
			if (dump.exists()) return
			dump.createNewFile()

			def total = extract(ch, 0)[1] as int
			total.times {
				int page = it + 1
				extract(ch, page)[0].each {
					dump.append "$it\n"
				}

				println "Progress (${ch.toUpperCase()}) - $page of $total (${page * 100 / total as int}%)"
				sleep 100
			}
		}

		new Merger("en")
	}

	static void pt() {
		def baseURL = "https://voc.iilp.cplp.org/index.php?action=simplesearch&base=form"

		def extract = (ch, i) -> {
			def content =  "$baseURL&query=$ch&sel=start&&start=$i".toURL().text
			int total = 100
			if (content =~ / \(de (\d+)\)<\/i>/) {
				total = (content =~ / \(de (\d+)\)<\/i>/)[0][1] as int
			}

			def words = []
			(content =~ /<a class=let href="\?action=(\w+?)&.+?><b>(.+?)<\/b>.+?>\w+<\/span>/).each {
				def act = it[1]
				def word = it[2]

				words << "$word,$act"
			}

			return [words, total]
		}

		def dir = new File("dicts_pt")
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

		new Merger("pt")
	}
}