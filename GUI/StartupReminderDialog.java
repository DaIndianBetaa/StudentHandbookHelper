import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StartupReminderDialog extends JDialog {


    public static void show(JFrame parent) {
        new StartupReminderDialog(parent).setVisible(true);
    }

    private StartupReminderDialog(JFrame parent) {
        super(parent, "Reminders", true);
        setSize(460, 380);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // ── Header ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 85, 145));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Upcoming & Overdue To-Dos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Here's what needs your attention today.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(200, 215, 240));

        titleStack.add(title);
        titleStack.add(Box.createVerticalStrut(2));
        titleStack.add(subtitle);
        header.add(titleStack, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ── Content ───────────────────────────────────────────────────────
        // Load urgent todos directly here — no need for a helper on TodoListView
        TodoList tl = new TodoList("todos.csv");
        tl.loadFromFile();
        ArrayList<TodoItem> urgent = new ArrayList<>();
        for (TodoItem item : tl.getTodos()) {
            if (!item.isCompleted() && item.isDueSoon())
                urgent.add(item);
        }

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(250, 250, 252));
        content.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        if (urgent.isEmpty()) {
            JLabel allGood = new JLabel("You're all caught up — nothing urgent due!");
            allGood.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            allGood.setForeground(new Color(60, 130, 70));
            allGood.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(Box.createVerticalStrut(20));
            content.add(allGood);
        } else {
            for (TodoItem item : urgent) {
                boolean overdue = item.daysTillDue() < 0;

                Color cardBg;
                Color accent;
                String badge;
                if (overdue) {
                    cardBg = new Color(255, 240, 240);
                    accent = new Color(200, 50, 50);
                    badge  = "OVERDUE " + (int) -item.daysTillDue() + "d";
                } else {
                    cardBg = new Color(255, 252, 220);
                    accent = new Color(180, 120, 0);
                    badge  = "Due in " + (int) item.daysTillDue() + "d";
                }

                JPanel card = new JPanel(new BorderLayout(10, 4));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
                card.setBackground(cardBg);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accent.brighter(), 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel textPanel = new JPanel();
                textPanel.setOpaque(false);
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                JLabel titleLbl = new JLabel(item.getTitle());
                titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                titleLbl.setForeground(new Color(30, 30, 30));

                String desc = item.getDescription();
                String descDisplay;
                if (desc != null && desc.length() > 55) {
                    descDisplay = desc.substring(0, 55) + "…";
                } else {
                    descDisplay = desc;
                }
                JLabel descLbl = new JLabel(descDisplay);
                descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                descLbl.setForeground(new Color(90, 90, 90));

                textPanel.add(titleLbl);
                textPanel.add(descLbl);

                JLabel badgeLbl = new JLabel(badge, SwingConstants.CENTER);
                badgeLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                badgeLbl.setForeground(Color.WHITE);
                badgeLbl.setOpaque(true);
                badgeLbl.setBackground(accent);
                badgeLbl.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                badgeLbl.setPreferredSize(new Dimension(100, 28));

                card.add(textPanel, BorderLayout.CENTER);
                card.add(badgeLbl,  BorderLayout.EAST);
                content.add(card);
                content.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        footer.setBackground(new Color(238, 238, 242));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 218)));

        JButton dismissBtn = new JButton("Dismiss");
        dismissBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dismissBtn.setBackground(new Color(45, 85, 145));
        dismissBtn.setForeground(Color.WHITE);
        dismissBtn.setFocusPainted(false);
        dismissBtn.setOpaque(true);
        dismissBtn.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        dismissBtn.addActionListener(e -> dispose());
        footer.add(dismissBtn);
        add(footer, BorderLayout.SOUTH);
    }
}

