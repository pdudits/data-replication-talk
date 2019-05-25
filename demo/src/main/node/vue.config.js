module.exports = {
    devServer: {
        port: 8082,
        proxy: {
            // plain string didn't work actually
            ".+": {
                target: 'http://localhost:8080/',
                ws: false
            }
        }
    }
}