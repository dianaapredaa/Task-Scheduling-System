/* Implement this class. */

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyDispatcher extends Dispatcher {

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }
    @Override
    public void addTask(Task task) {
        // Use the specified scheduling algorithm to assign the task to a host
        switch (algorithm) {
            case ROUND_ROBIN ->
                // For RoundRobin, assign the task to the next host in a circular manner
                    roundRobin(task);
            case SHORTEST_QUEUE ->
                // For ShortestQueue, assign the task to the host with the shortest queue
                    shortestQueue(task);
            case LEAST_WORK_LEFT ->
                // For LeastWorkLeft, assign the task to the host with the least work left
                    leastWorkLeft(task);
            case SIZE_INTERVAL_TASK_ASSIGNMENT ->
                // For Size Interval Task Assignment, assign the task to the host with the corresponding index
                    sizeIntervalTaskAssignment(task);
        }
    }

    private int currentHostId = 0;
    private void roundRobin(Task task) {
        // Determine the index of the next available host in a circular manner
        int nextNodeId = currentHostId % hosts.size();

        // Assign the task to the next host
        Host nextNode = hosts.get(nextNodeId);
        nextNode.addTask(task);

        // Update lastAssignedHostIndex
        currentHostId++;
    }

    private void shortestQueue(Task task) {
        // Use Comparator and Collections.min to find the host with the shortest queue
        Host shortestQueueHost = hosts.isEmpty() ?
                null :
                Collections.min(hosts, Comparator.comparingInt(Host::getQueueSize));

        if (shortestQueueHost != null) {
            // Assign the task to the host with the shortest queue
            shortestQueueHost.addTask(task);
        } else {
            // Handle the case when hosts list is empty
            System.out.println("No hosts available to assign the task.");
        }
    }

    private void sizeIntervalTaskAssignment(Task task) {
        // Define an array to map task types to host indices
        int[] typeToIndexMap = {0, 1, 2};

        // Retrieve the task type
        TaskType size = task.getType();

        // Determine the index of the host to which the task should be assigned to
        int index = typeToIndexMap[size.ordinal()];

        // Assign the task to the selected host
        Host host = hosts.get(index);
        host.addTask(task);
    }

    private void leastWorkLeft(Task task) {
        // Determine the host with the least work left
        Host leastWorkLeftHost = hosts.stream()
                .min(Comparator.comparingLong(Host::getWorkLeft)).orElse(null);

        // Check if there is a host with work left
        if (leastWorkLeftHost != null) {
            // Assign the task to the host with the least work left
            leastWorkLeftHost.addTask(task);
        } else {
            // Handle the case when hosts list is empty
            System.out.println("No hosts available to assign the task.");
        }
    }
}
