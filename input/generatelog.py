import os
import random
with open('./userinfo.txt.txt') as f:
    userlist = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line
userlist = [x.strip() for x in userlist] 
print(userlist)
all_files = os.listdir("../input");
#print(all_files);
html_files = filter(lambda x: x[-5:] == '.html', all_files);

print(html_files);
html_files_list = [];
for x in html_files:
	html_files_list.append(x);
print(html_files_list)
for time in range(10):
	print(random.randint(1,101));


secure_random = random.SystemRandom()
print(secure_random.choice(userlist))
print(secure_random.choice(html_files_list))

thefile = open('history_log', 'a');
info =""
for iter in range(100):
	info += secure_random.choice(userlist) +"\t" + secure_random.choice(html_files_list) + "\t" + str(random.randint(1,101)) + "\n";
    
thefile.write(info);

