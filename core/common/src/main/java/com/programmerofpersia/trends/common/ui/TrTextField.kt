package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle.Default,
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    val colors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            /*.background(
                color = colors.,
                shape = RoundedCornerShape(22.dp)
                *//*shape = MaterialTheme.shapes.small*//*
            )*/
            .indicatorLine(
                enabled = enabled,
                isError = false,
                interactionSource = interactionSource,
                colors = colors,
                // Hide the indicator line:
                focusedIndicatorLineThickness = 0.dp,
                // Hide the indicator line:
                unfocusedIndicatorLineThickness = 0.dp
            )
            .fillMaxWidth()
            .wrapContentHeight(),
        textStyle = textStyle,
        singleLine = singleLine
    ) { innerTextField ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = MaterialTheme.colorScheme.primary),
            /*verticalAlignment = Alignment.CenterVertically,*/
        ) {

            val (startIcon, centerTextField, endIcon) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(startIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        visibility =
                            if (leadingIcon != null) Visibility.Visible else Visibility.Gone
                    }
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
            }


            Box(
                modifier = Modifier
                    .constrainAs(centerTextField) {
                        start.linkTo(startIcon.end)
                        end.linkTo(endIcon.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                        top = 10.dp, bottom = 10.dp
                    ),
                    placeholder = placeholder,
                    /*leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,*/
                    colors = colors,
                    /*container = {
                        TextFieldDefaults.OutlinedBorderContainerBox(
                            enabled = enabled,
                            isError = false,
                            colors = colors,
                            interactionSource = interactionSource,
                            shape = MaterialTheme.shapes.small,
                            unfocusedBorderThickness = 2.dp,
                            focusedBorderThickness = 4.dp
                        )
                    },*/
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(endIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        visibility =
                            if (trailingIcon != null) Visibility.Visible else Visibility.Gone
                    }
            ) {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }

        }

    }

}