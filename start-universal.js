#!/usr/bin/env node

/**
 * Universal Starter - Detects OS and runs appropriate script
 */

const { spawn } = require('child_process');
const path = require('path');

const isWindows = process.platform === 'win32';

console.log(`\nDetected OS: ${process.platform}\n`);

const script = isWindows ? 'quick-start.bat' : './quick-start.sh';
const shell = isWindows ? true : false;

const proc = spawn(script, [], {
    cwd: __dirname,
    stdio: 'inherit',
    shell
});

proc.on('error', (err) => {
    console.error(`Failed to start: ${err.message}`);
    process.exit(1);
});

proc.on('exit', (code) => {
    process.exit(code || 0);
});
