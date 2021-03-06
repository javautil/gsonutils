set -x
PWD=`pwd`
current_dir=`basename $PWD`
echo current_dir $current_dir
project=$current_dir
echo project $project
if [ ! $project = $current_dir ] ; then
   echo project $project is not current_dir $current_dir  >&2
fi 


 
#git init
cp ~/templates/.gitignore .gitignore
git config --global user.email "javautil.org@gmail.com"
git remote add origin ssh://git@github.com/javautil/$project
git remote set-url origin ssh://git@github.com/javautil/$project
git config --get remote.origin.url
git remote -v
git add -A .
git commit
git push -u origin master
