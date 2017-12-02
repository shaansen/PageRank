import os
import random
all_files = os.listdir("../input");

html_files = filter(lambda x: x[-5:] == '.html', all_files);

html_files_list = [];
for x in html_files:
	html_files_list.append(x);
print(html_files_list)

domainlist = [];
domainlist = [https://www.nationalgeography.com/, https://www.fantasticbeasts.org/,
https://www.webopedia.gov/, https://www.wikipedia.org/, https://www.interclix.net/, https://www.nationalgeography.edu/, https://www.nationalgeography.co.uk/,https://www.animalplanet.in/,
https://www.livinginternet.co/];

secure_random = random.SystemRandom()

thefile = open('domain_log', 'a');
info =""
for iter in range(100):
	fileName = secure_random.choice(html_files_list);
	info += fileName +"\t" + secure_random.choice(domainlist) + fileName + "\n";
    
thefile.write(info);