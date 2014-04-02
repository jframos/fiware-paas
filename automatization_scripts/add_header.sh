for f in **/*.py; do
  echo $f
  cat ../src/license/fiware/header_py.txt $f > $f.new
  mv $f.new $f
done
