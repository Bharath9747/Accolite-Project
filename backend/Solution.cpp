#include <bits/stdc++.h>
using namespace std;
int longestPalindrome(string s) {
	int oddCount = 0;
        unordered_map<char, int> ump;
        for(char ch : s) {
            ump[ch]++;
            if (ump[ch] % 2 == 1)
                oddCount++;
            else    
                oddCount--;
        }
        if (oddCount > 1)
            return s.length() - oddCount + 1;
        return s.length();
}
int main()
{
	string s;
	cin>>s;
	cout<<longestPalindrome(s);
}