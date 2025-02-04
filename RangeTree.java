import java.util.List;

public class RangeTree{
    private OrderedDeletelessDictionary<Double, Range> byStart;
    private OrderedDeletelessDictionary<Double, Range> byEnd;
    private int size;

    public RangeTree(){
        byStart = new AVLTree<>();
        byEnd = new AVLTree<>();
        size = 0;
    }
    
    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    // Return the Range which starts at the given time
    // The running time is O(log n)
    public Range findByStart(Double start){
        return byStart.find(start);
    }

    // Return the Range which ends at the given time
    // The running time is O(log n)
    public Range findByEnd(Double end){
        return byEnd.find(end);
    }

    // Gives a list of Ranges sorted by start time.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Range> getRanges(){
        return byStart.getValues();
    }

    // Gives a sorted list of start times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getStartTimes(){
        return byStart.getKeys();
    }

    // Gives a sorted list of end times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getEndTimes(){
        return byEnd.getKeys();
    }

    // Identifies whether or not the given range conflicts with any
    // ranges that are already in the data structure.
    // If the data structure is empty, then it does not conflict
    // with any ranges, so we should return false.
    // The running time of this method should be O(log n)
    public boolean hasConflict(Range query){
        // If the range tree is empty there is no conflict
        if (isEmpty()) {
            return false;
        }

        // An event only conflicts if it ends or starts between another event's start and end
        // first check that the new event doesn't end in the middle of an existing event
        Double end_prev_start = byStart.findPrevKey(query.end);
        Double end_next_end = byEnd.findNextKey(query.end);
        // If one of the values are null than the new event doesn't end between another
        if (end_prev_start != null && end_next_end != null) {
            // Get the range associated with the keys and if they are equal then the new event conflicts
            Range prev_event = byStart.find(end_prev_start);
            Range next_event = byEnd.find(end_next_end);
            if (prev_event.equals(next_event)) {
                return true;
            }   
        }

        // Next check that the new event doesn't start between another event's start and end
        Double start_prev_start = byStart.findPrevKey(query.start);
        Double start_next_end = byEnd.findNextKey(query.start);
        // If one of the values are null than the new event doesn't end between another
        if (start_prev_start != null && start_next_end != null) {
            // Get the range associated with the keys and if they are equal then the new event conflicts
            Range prev_event = byStart.find(start_prev_start);
            Range next_event = byEnd.find(start_next_end);
            if (prev_event.equals(next_event)) {
                return true;
            }
        }

        // Then check that an existing event doesn't surround the new event
        // If one of the the values are null than an existing event doesn't surround the new event
        if (start_prev_start != null && end_next_end != null) {
            // Get the range associated with the keys and if they are equal the new event conflicts
            Range prev_event = byStart.find(start_prev_start);
            Range next_event = byEnd.find(end_next_end);
            if (prev_event.equals(next_event)) {
                return true;
            }
        }

        // Finally check that our new event doesn't surround an existing event
        // If one of the the values are null than the new event doesn't surround an existing event
        if (end_prev_start != null && start_next_end != null) {
            // Get the range associated with the keys and if they are equal the new event conflicts
            Range prev_event = byStart.find(end_prev_start);
            Range next_event = byEnd.find(start_next_end);
            if (prev_event.equals(next_event)) {
                return true;
            }
        }

        // If we passed all the tests then the event doesn't collide
        return false;
    }

    // Inserts the given range into the data structure if it has no conflict.
    // Does not modify the data structure if it does have a conflict.
    // Return value indicates whether or not the item was successfully
    // added to the data structure.
    // Running time should be O(log n)
    public boolean insert(Range query){
        // If there is a conflict return false
        if (hasConflict(query)) {
            return false;
        }
        // Otherwise add by start and by end into the AVL Trees and increment size
        byStart.insert(query.start, query);
        byEnd.insert(query.end, query);
        size++;
        return true;
    }
}
