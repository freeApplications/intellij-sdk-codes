package jp.freeapps.intellij.plugin.phparray.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBRadioButton
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.*
import java.awt.GridBagLayout
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSeparator

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent : JPanel(GridBagLayout()) {
    // constants
    private val leftInset = 20
    private val rightInset = 10
    private val bottomInset = 4

    // variables
    private var lineCount = 0

    // PHP Array syntax
    @Suppress("DialogTitleCapitalization")
    private val useArrayOption = JBRadioButton("array( )")
    private val useBraketOption = JBRadioButton("[ ]")
    private val arraySyntax: Array<JBRadioButton> = arrayOf(useArrayOption, useBraketOption)

    // String quotation marks
    private val useSingleQuoteOption = JBRadioButton("Single quotes")
    private val useDoubleQuoteOption = JBRadioButton("Double quotes")
    private val stringQuotationMarks: Array<JBRadioButton> = arrayOf(useSingleQuoteOption, useDoubleQuoteOption)

    val preferredFocusedComponent: JComponent
        get() = useArrayOption

    var useBraket: Boolean
        get() = useBraketOption.isSelected
        set(newStatus) {
            useBraketOption.isSelected = newStatus
            useArrayOption.isSelected = !newStatus
        }

    var useDoubleQuote: Boolean
        get() = useDoubleQuoteOption.isSelected
        set(newStatus) {
            useDoubleQuoteOption.isSelected = newStatus
            useSingleQuoteOption.isSelected = !newStatus
        }

    init {
        // JSON to PHP Array Settings
        addTitle("JSON to PHP Array")
        addOptions(
            mapOf(
                "PHP Array syntax : " to arraySyntax,
                "String quotation marks : " to stringQuotationMarks,
            )
        )
        groupingRadioButtons(arraySyntax)
        groupingRadioButtons(stringQuotationMarks)

        // fill vertically
        val insets = JBUI.insets(0, 0, 0, 0)
        val constraints = GridBagConstraints(0, lineCount, 1, 1, 1.0, 1.0, WEST, BOTH, insets, 0, 0)
        this.add(JPanel(), constraints)
    }

    private fun addTitle(title: String) {
        val titlePanel = JPanel(GridBagLayout())

        var insets = JBUI.insets(0, 0, 0, rightInset)
        var constraints = GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0)
        titlePanel.add(JBLabel(title), constraints)

        insets = JBUI.insets(0, 0, 0, 0)
        constraints = GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0)
        titlePanel.add(JSeparator(JSeparator.HORIZONTAL), constraints)

        insets = JBUI.insets(0, 0, bottomInset, 0)
        constraints = GridBagConstraints(0, lineCount++, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0)
        this.add(titlePanel, constraints)
    }

    private fun <T : JComponent> addOptions(options: Map<String, Array<T>>) {
        val optionsPanel = JPanel(GridBagLayout())
        val maxColumns = options.values.fold(0) { accumulator, items ->
            if (items.size > accumulator) items.size else accumulator
        } + 1

        var rowCount = 0
        options.forEach { (label, items) ->
            var insets = JBUI.insets(0, 0, bottomInset, rightInset)
            var constraints = GridBagConstraints(0, rowCount, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0)
            optionsPanel.add(JBLabel(label), constraints)
            items.forEachIndexed { index, item ->
                constraints = GridBagConstraints(index + 1, rowCount, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0)
                if (index + 1 == items.size) {
                    constraints.gridwidth = maxColumns - items.size
                    constraints.weightx = 1.0
                    constraints.fill = HORIZONTAL
                    insets = JBUI.insets(0, 0, bottomInset, 0)
                }
                optionsPanel.add(item, constraints)
            }
            rowCount++
        }
        val insets = JBUI.insets(0, leftInset, bottomInset, 0)
        val constraints = GridBagConstraints(0, lineCount++, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0)
        this.add(optionsPanel, constraints)
    }

    private fun groupingRadioButtons(radioButtons: Array<JBRadioButton>) {
        val buttonGroup = ButtonGroup()
        radioButtons.forEach { radioButton -> buttonGroup.add(radioButton) }
    }
}