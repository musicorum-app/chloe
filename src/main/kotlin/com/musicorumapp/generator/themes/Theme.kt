package com.musicorumapp.generator.themes

import java.awt.image.BufferedImage

interface Theme {
    fun generate(): BufferedImage

    fun generateStory(): BufferedImage
}