import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Mini-Projet IHM - Convertisseur Dynamique
 * Université Cadi Ayyad - Licence SIR 2025-2026
 *
 * Catégories disponibles :
 *   1. Température  (Celsius, Fahrenheit, Kelvin)
 *   2. Distance     (Mètre, Kilomètre, Mille)
 */
public class ConvertisseurDynamique extends JFrame {

    // =========================================================================
    // COMPOSANTS DE L'INTERFACE
    // =========================================================================

    private JComboBox<String> comboDepart;   // liste déroulante unité de départ
    private JComboBox<String> comboArrivee;  // liste déroulante unité d'arrivée
    private JTextField champValeur;          // champ de saisie de la valeur
    private JLabel labelResultat;            // label pour afficher le résultat
    private JLabel labelCategorie;           // affiche la catégorie active

    // =========================================================================
    // DONNÉES : tableaux des unités pour chaque catégorie
    // =========================================================================

    private String[] unitesTemperature = { "Celsius", "Fahrenheit", "Kelvin" };
    private String[] unitesDistance    = { "Mètre", "Kilomètre", "Mille" };

    // La catégorie active au démarrage
    private String categorieActive = "Température";

    // =========================================================================
    // CONSTRUCTEUR
    // =========================================================================
    public ConvertisseurDynamique() {

        // --- Configuration de la fenêtre ---
        setTitle("Convertisseur Multi-Catégories");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── 1. BARRE DE MENU ──────────────────────────────────────────────────
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCategories = new JMenu("Catégories");

        // --- Item : Température ---
        JMenuItem itemTemperature = new JMenuItem("Température");
        itemTemperature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                categorieActive = "Température";
                mettreAJourUnites(unitesTemperature);
            }
        });

        // --- Item : Distance ---
        JMenuItem itemDistance = new JMenuItem("Distance");
        itemDistance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                categorieActive = "Distance";
                mettreAJourUnites(unitesDistance);
            }
        });

        menuCategories.add(itemTemperature);
        menuCategories.add(itemDistance);
        menuBar.add(menuCategories);
        setJMenuBar(menuBar);

        // ── 2. PANNEAU PRINCIPAL ──────────────────────────────────────────────
        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        panneauPrincipal.setBackground(new Color(245, 247, 250));

        // ── 3. LABEL CATÉGORIE ACTIVE ─────────────────────────────────────────
        labelCategorie = new JLabel("Catégorie : Température");
        labelCategorie.setFont(new Font("Arial", Font.BOLD, 13));
        labelCategorie.setForeground(new Color(60, 60, 60));
        labelCategorie.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauPrincipal.add(labelCategorie);
        panneauPrincipal.add(Box.createVerticalStrut(12));

        // ── 4. LIGNE DE SAISIE ────────────────────────────────────────────────
        JPanel ligneValeur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ligneValeur.setBackground(new Color(245, 247, 250));
        ligneValeur.add(new JLabel("Valeur : "));

        champValeur = new JTextField(12);
        champValeur.setFont(new Font("Arial", Font.PLAIN, 13));
        ligneValeur.add(champValeur);

        panneauPrincipal.add(ligneValeur);
        panneauPrincipal.add(Box.createVerticalStrut(8));

        // ── 5. LIGNE DES UNITÉS (De / Vers) ──────────────────────────────────
        JPanel ligneUnites = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ligneUnites.setBackground(new Color(245, 247, 250));

        ligneUnites.add(new JLabel("De : "));

        // DefaultComboBoxModel permet de changer les éléments via setModel()
        // sans détruire et recréer le JComboBox
        comboDepart = new JComboBox<String>(new DefaultComboBoxModel<String>(unitesTemperature));
        comboDepart.setFont(new Font("Arial", Font.PLAIN, 12));
        ligneUnites.add(comboDepart);

        ligneUnites.add(new JLabel("  Vers : "));

        comboArrivee = new JComboBox<String>(new DefaultComboBoxModel<String>(unitesTemperature));
        comboArrivee.setFont(new Font("Arial", Font.PLAIN, 12));
        ligneUnites.add(comboArrivee);

        panneauPrincipal.add(ligneUnites);
        panneauPrincipal.add(Box.createVerticalStrut(8));

        // ── 6. LIGNE DES BOUTONS ──────────────────────────────────────────────
        JPanel lignesBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        lignesBoutons.setBackground(new Color(245, 247, 250));

        // Bouton Convertir
        JButton boutonConvertir = new JButton("Convertir");
        boutonConvertir.setFont(new Font("Arial", Font.BOLD, 12));
        boutonConvertir.setBackground(new Color(0, 102, 204));
        boutonConvertir.setForeground(Color.WHITE);
        boutonConvertir.setFocusPainted(false);
        boutonConvertir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lancerConversion();
            }
        });
        lignesBoutons.add(boutonConvertir);

        // Bouton Changer direction
        JButton boutonInverser = new JButton("Changer direction");
        boutonInverser.setFont(new Font("Arial", Font.PLAIN, 12));
        boutonInverser.setFocusPainted(false);
        boutonInverser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inverserUnites();
            }
        });
        lignesBoutons.add(boutonInverser);

        panneauPrincipal.add(lignesBoutons);
        panneauPrincipal.add(Box.createVerticalStrut(15));

        // ── 7. LABEL DE RÉSULTAT ──────────────────────────────────────────────
        labelResultat = new JLabel(" ");
        labelResultat.setFont(new Font("Arial", Font.BOLD, 15));
        labelResultat.setForeground(new Color(0, 130, 70));
        labelResultat.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauPrincipal.add(labelResultat);

        add(panneauPrincipal);
        setVisible(true);
    }

    // =========================================================================
    // MÉTHODE : Mettre à jour les listes déroulantes
    // =========================================================================
    /**
     * Remplace le contenu des deux JComboBox avec les nouvelles unités.
     * setModel() + DefaultComboBoxModel : on ne recrée pas les composants,
     * on change juste leurs données.
     */
    private void mettreAJourUnites(String[] unites) {
        comboDepart.setModel(new DefaultComboBoxModel<String>(unites));
        comboArrivee.setModel(new DefaultComboBoxModel<String>(unites));
        labelCategorie.setText("Catégorie : " + categorieActive);
        labelResultat.setText(" ");
        champValeur.setText("");
    }

    // =========================================================================
    // MÉTHODE : Inverser les deux unités sélectionnées
    // =========================================================================
    private void inverserUnites() {
        int indexDepart  = comboDepart.getSelectedIndex();
        int indexArrivee = comboArrivee.getSelectedIndex();
        comboDepart.setSelectedIndex(indexArrivee);
        comboArrivee.setSelectedIndex(indexDepart);
    }

    // =========================================================================
    // MÉTHODE : Lancer la conversion
    // =========================================================================
    /**
     * 1. Vérifie que le champ n'est pas vide
     * 2. Vérifie que la saisie est un nombre valide
     * 3. Appelle la bonne méthode selon la catégorie active
     * 4. Affiche le résultat
     */
    private void lancerConversion() {

        String texte = champValeur.getText().trim();

        // Vérification 1 : champ vide
        if (texte.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez entrer une valeur !",
                    "Champ vide",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Vérification 2 : est-ce un nombre ?
        double valeur;
        try {
            valeur = Double.parseDouble(texte);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Saisie invalide ! Veuillez entrer un nombre.",
                    "Erreur de saisie",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String unitDepart  = (String) comboDepart.getSelectedItem();
        String unitArrivee = (String) comboArrivee.getSelectedItem();

        double resultat;

        if (categorieActive.equals("Température")) {
            resultat = convertirTemperature(valeur, unitDepart, unitArrivee);
        } else {
            resultat = convertirDistance(valeur, unitDepart, unitArrivee);
        }

        labelResultat.setText(
                String.format("%.4f %s  =  %.4f %s", valeur, unitDepart, resultat, unitArrivee)
        );
    }

    // =========================================================================
    // MÉTHODE : Conversion de Température  (Pivot = Celsius)
    // =========================================================================
    /**
     * Étape 1 : unité de départ → Celsius
     * Étape 2 : Celsius → unité d'arrivée
     *
     * Fahrenheit → Celsius : (F - 32) * 5/9
     * Kelvin     → Celsius : K - 273.15
     * Celsius → Fahrenheit : (C * 9/5) + 32
     * Celsius → Kelvin     : C + 273.15
     */
    private double convertirTemperature(double valeur, String de, String vers) {

        // Étape 1 : convertir vers Celsius (pivot)
        double celsius;
        if (de.equals("Celsius")) {
            celsius = valeur;
        } else if (de.equals("Fahrenheit")) {
            celsius = (valeur - 32) * 5.0 / 9.0;
        } else { // Kelvin
            celsius = valeur - 273.15;
        }

        // Étape 2 : convertir depuis Celsius
        double resultat;
        if (vers.equals("Celsius")) {
            resultat = celsius;
        } else if (vers.equals("Fahrenheit")) {
            resultat = (celsius * 9.0 / 5.0) + 32;
        } else { // Kelvin
            resultat = celsius + 273.15;
        }

        return resultat;
    }

    // =========================================================================
    // MÉTHODE : Conversion de Distance  (Pivot = Mètre)
    // =========================================================================
    /**
     * Étape 1 : unité de départ → Mètre
     * Étape 2 : Mètre → unité d'arrivée
     *
     * Kilomètre → Mètre : km * 1000
     * Mille     → Mètre : mile * 1609.34
     * Mètre → Kilomètre : m / 1000
     * Mètre → Mille     : m / 1609.34
     */
    private double convertirDistance(double valeur, String de, String vers) {

        // Étape 1 : convertir vers Mètre (pivot)
        double metre;
        if (de.equals("Mètre")) {
            metre = valeur;
        } else if (de.equals("Kilomètre")) {
            metre = valeur * 1000;
        } else { // Mille
            metre = valeur * 1609.34;
        }

        // Étape 2 : convertir depuis Mètre
        double resultat;
        if (vers.equals("Mètre")) {
            resultat = metre;
        } else if (vers.equals("Kilomètre")) {
            resultat = metre / 1000;
        } else { // Mille
            resultat = metre / 1609.34;
        }

        return resultat;
    }

    // =========================================================================
    // POINT D'ENTRÉE DU PROGRAMME
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ConvertisseurDynamique();
            }
        });
    }
}