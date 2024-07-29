package com.kuuhaku

import org.apache.commons.lang3.StringUtils

class Merger {
	Merger(String locale) {
		def dir = new File("dicts_$locale")
		if (!dir.exists() || !dir.directory) return

		def dict = new File("dict_full_${locale}.txt")
		if (!dict.exists() || !dict.file) dict.createNewFile()

		def groups = [:] as Map<String, List<String>>
		def words = [] as Set<String>
		dir.listFiles().each {
			it.eachLine {
				def (word, act) = it.split(",") as List<String>

				word = StringUtils.stripAccents(word.split(" ")[1]).toLowerCase()
				if (word =~ /\W/ || word.length() < 3) return

				groups.computeIfAbsent(act, k -> []) << word
			}

			def lemma = groups["lemma"]
			def fna = groups["fna"]
			if (fna) {
				lemma.removeAll(fna)
			}

			println "Joined $it"
			words.addAll(groups["lemma"])
		}

		dropDeadends(words)

		dict.text = words.join("\n")
	}

	static void dropDeadends(Collection<String> words) {
		def correct = false
		int prcnt = 0

		parent: while (!correct) {
			for (final i in 0..<words.size()) {
				def word = words[i]
				def ending = word.substring(word.length() - 2)
				if (words.count { it.startsWith(ending) } < 3) {
					words.removeIf {
						if (it.endsWith(ending)) {
							println "Dropped $it"
							return true
						}

						return false
					}

					prcnt = 0
					continue parent
				}

				int curr = i * 100 / words.size() as int
				if (curr > prcnt) {
					prcnt = curr
					println "Processing... $i/${words.size()} ($curr%)"
				}
			}

			correct = true
		}
	}
}