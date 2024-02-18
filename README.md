### Thread Task Maker

    Student: Diana Preda  
    Group: 334CA

### Description
This project implements a task scheduling system that allows tasks to be assigned to hosts based on different scheduling algorithms. My implementation consists of two main classes: `MyHost` representing a host capable of processing tasks, and `MyDispatcher` representing a task dispatcher responsible for assigning tasks to hosts.

### Implementation

#### MyHost
`MyHost` extends the abstract class `Host` and utilizes a `PriorityBlockingQueue` to manage the task queue. It implements task execution, priority-based preemptive scheduling, and provides methods for adding tasks, retrieving the queue size, and checking the remaining work time.

#### MyDispatcher
`MyDispatcher` extends the abstract class `Dispatcher` and supports multiple scheduling algorithms: Round Robin, Shortest Queue, Least Work Left, and Size Interval Task Assignment. It allows adding tasks to the system, and the appropriate scheduling algorithm determines the host assignment.

### Scheduling Algorithms

1. *Round Robin*: Assign tasks to hosts in a circular manner.
2. *Shortest Queue*: Assign tasks to the host with the shortest task queue.
3. *Least Work Left*: Assign tasks to the host with the least remaining work.
4. *Size Interval Task Assignment*: Assign tasks based on their type to specific hosts.

### Main Features for MyHost
`Scheduling Algorithms`: Implements multiple scheduling algorithms (Round Robin, Shortest Queue, Least Work Left, Size Interval Task Assignment).

`Dynamic Task Assignment`: Assigns tasks to hosts based on the selected scheduling algorithm.

`Flexible Configuration`: Supports adding hosts and tasks dynamically.

### Main Features for MyDispatcher
`Task Queue`: The class uses a PriorityBlockingQueue to store tasks. Tasks are sorted based on priority and ID.

`Thread Safety`: The implementation ensures thread safety using atomic variables (AtomicBoolean, AtomicReference, AtomicLong) and synchronized blocks.

`Preemptive Scheduling`: If a higher-priority task is added while a task is running, the running task is preempted.

`Dynamic Remaining Work Time`: The remaining work time is dynamically adjusted during task execution.

### Improvements
- Improve exception handling by providing more meaningful error messages and logging the exceptions.
- Consider using a specific exception type instead of catching the generic Exception.

### Conclusion
This project was a great opportunity to learn more about concurrency and multithreading in Java. I enjoyed implementing the scheduling algorithms and experimenting with different approaches. I also learned about the importance of thread safety and how to ensure it in Java.