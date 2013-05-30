#!/bin/sh

cat - > $(dirname $0)/guess.out

cat -<<EOF
age=11
gender=male
EOF