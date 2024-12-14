import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Block class to represent individual memory blocks
class Block {
    private final int size; // Memory block size in KB
    private boolean allocated; // Whether the memory block is allocated or free

    public Block(int size) {
        this.size = size;
        this.allocated = false;
    }

    public int getSize() {
        return size; // Getter for block size
    }

    public boolean isAllocated() {
        return allocated; // Getter for allocation status
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated; // Setter to change the allocation status
    }
}

// MemoryManager class for allocation and deallocation logic
class MemoryManager {
    private static final Map<Integer, Deque<Block>> freeLists = new HashMap<>(); // Map of free memory lists

    static {
        // Memory block sizes initialization
        int[] sizes = {32, 64, 128, 256, 512};
        for (int size : sizes) {
            freeLists.put(size, new LinkedList<>()); // Create a free list for each block size
            for (int i = 0; i < 10; i++) { // Simulate free memory blocks
                freeLists.get(size).add(new Block(size)); // Populate free lists with 10 blocks of each size
            }
        }
    }

    // Simulate memory allocation by polling a free block from the respective size's list
    public static Block allocate(int size) {
        if (freeLists.containsKey(size) && !freeLists.get(size).isEmpty()) {
            Block block = freeLists.get(size).poll(); // Get a free block
            if (block != null) {
                block.setAllocated(true); // Mark the block as allocated
                return block;
            }
        }
        return null; // Return null if allocation fails
    }

    // Simulate memory deallocation by adding a block back to the free list
    public static void deallocate(int size) {
        if (freeLists.containsKey(size)) {
            Block block = new Block(size); // Create a new memory block
            block.setAllocated(false); // Mark it as free
            freeLists.get(size).add(block); // Add back to the free list
        }
    }

    // Display memory status (number of free memory blocks of each size)
    public static String displayStatus() {
        StringBuilder status = new StringBuilder();
        for (Map.Entry<Integer, Deque<Block>> entry : freeLists.entrySet()) {
            status.append("Size ").append(entry.getKey()).append(" KB: ")
                    .append(entry.getValue().size()).append(" Free Blocks\n"); // Construct status string
        }
        return status.toString();
    }

    public static boolean containsKey(int size) {
        return freeLists.containsKey(size); // Check if the block size exists
    }
}

// Main Application Class for GUI
public class MemoryManagerApp {
    private final JFrame frame; // Main application window

    public MemoryManagerApp() {
        // Set up the main application window
        frame = new JFrame("Quick Fit Memory Management Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new BorderLayout());
        frame.setContentPane(mainPanel);

        // Title Label setup
        JLabel titleLabel = new JLabel("Quick Fit Memory Management Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel with buttons for user interaction
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Create buttons
        JButton displayStatusButton = createStyledButton("Display Memory Status");
        JButton allocateButton = createStyledButton("Allocate Block");
        JButton deallocateButton = createStyledButton("Deallocate Block");
        JButton exitButton = createStyledButton("Exit");

        // Add event listeners to buttons
        displayStatusButton.addActionListener(e -> handleDisplayStatus());
        allocateButton.addActionListener(e -> handleAllocate());
        deallocateButton.addActionListener(e -> handleDeallocate());
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the button panel
        buttonPanel.add(displayStatusButton);
        buttonPanel.add(allocateButton);
        buttonPanel.add(deallocateButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true); // Show the GUI
    }

    // Styled button creation utility
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return button;
    }

    // Handle display memory status action
    private void handleDisplayStatus() {
        JOptionPane.showMessageDialog(frame, MemoryManager.displayStatus(), "Memory Status", JOptionPane.INFORMATION_MESSAGE);
    }

    // Handle memory allocation
    private void handleAllocate() {
        String input = JOptionPane.showInputDialog(frame, "Enter Block Size To Allocate (32KB, 64KB, 128KB, 256KB, 512KB):");
        if (input != null) {
            try {
                int size = Integer.parseInt(input);
                if (MemoryManager.containsKey(size)) {
                    Block allocatedBlock = MemoryManager.allocate(size);
                    JOptionPane.showMessageDialog(frame,
                            allocatedBlock != null ? "Memory Block Allocated: " + size + " KB" : "No Free Block Available.",
                            "Allocate Block", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Size Input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Enter a Valid Number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Handle memory deallocation
    private void handleDeallocate() {
        String input = JOptionPane.showInputDialog(frame, "Enter Block Size To Deallocate (32KB, 64KB, 128KB, 256KB, 512KB):");
        if (input != null) {
            try {
                int size = Integer.parseInt(input);
                if (MemoryManager.containsKey(size)) {
                    MemoryManager.deallocate(size);
                    JOptionPane.showMessageDialog(frame, "Block Deallocated Successfully: " + size + " KB", "Deallocate Block", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Size Input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Enter a Valid Number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryManagerApp::new); // Start the GUI application
    }
}
