from collections import deque

q = deque()
q.append("Hello, World!")
q.append("안녕하세요!")
print(q[0])
q.popleft()
print(q[0])
if len(q) >0:
    print("queue is not Empty")