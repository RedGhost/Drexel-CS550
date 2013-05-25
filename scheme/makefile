pager=less
scheme=mit-scheme

# You can name the the files as you wish. Your interpreter will read stdin, as previously.
# view-p4 — Displays all of the files required for part 4 using less.
# run-p4 — Displays the result of the static environment interpreter and then the dynamic environment interpreter.

view-p4 : ch4-mceval.scm ch4-mceval-dynamic.scm input/part4-static.scm input/part4-dynamic.scm
	-$(pager) ch4-mceval.scm
	-$(pager) ch4-mceval-dynamic.scm
	-$(pager) input/part4-static.scm
	-$(pager) input/part4-dynamic.scm

run-p4 : ch4-mceval.scm ch4-mceval-dynamic.scm input/part4-static.scm input/part4-dynamic.scm
	$(scheme) < input/part4-static.scm | $(pager)
	$(scheme) < input/part4-dynamic.scm | $(pager)
